package com.angelfg.spring_security_course.configs.authorization;

import com.angelfg.spring_security_course.exceptions.ObjectNotFoundException;
import com.angelfg.spring_security_course.persistence.entities.security.GrantedPermission;
import com.angelfg.spring_security_course.persistence.entities.security.OperationEntity;
import com.angelfg.spring_security_course.persistence.entities.security.User;
import com.angelfg.spring_security_course.persistence.repositories.security.OperationRepository;
import com.angelfg.spring_security_course.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final OperationRepository operationRepository;
    private final UserService userService;

    @Override
    public AuthorizationDecision check(
            Supplier<Authentication> authentication,
            RequestAuthorizationContext requestContext
    ) {
        HttpServletRequest request = requestContext.getRequest();
        // System.out.println(request.getRequestURL()); // http://localhost:9191/api/v1/products/1
        // System.out.println(request.getRequestURI()); // /api/v1/products/1

        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);
        if (isPublic) return new AuthorizationDecision(true);

        boolean isGranted = isGranted(url, httpMethod, authentication.get());
        return new AuthorizationDecision(isGranted);
    }

    private String extractUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String url = request.getRequestURI();

        url = url.replace(contextPath, "");

        System.out.println(url);
        return url;
    }

    private boolean isPublic(String url, String httpMethod) {
        List<OperationEntity> publicAccessEndpoints = operationRepository.findByPubliccAcces();

        boolean isPublic = publicAccessEndpoints.stream().anyMatch(getOperationPredicate(url, httpMethod));

        System.out.println("IS PUBLIC: " + isPublic);
        return isPublic;
    }

    private static Predicate<OperationEntity> getOperationPredicate(String url, String httpMethod) {
        return operation -> {
            String basePath = operation.getModule().getBasePath();

            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            Matcher matcher = pattern.matcher(url);

            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {

        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

        List<OperationEntity> operations = obtainOperations(authentication);

        boolean isGranted = operations.stream().anyMatch(getOperationPredicate(url, httpMethod));

        System.out.println("IS GRANTED: " + isGranted);
        return isGranted;
    }

    private List<OperationEntity> obtainOperations(Authentication authentication) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authToken.getPrincipal();

        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        return user.getRole().getPermissions()
                .stream()
                .map(GrantedPermission::getOperation)
                .collect(Collectors.toList());
    }

}
