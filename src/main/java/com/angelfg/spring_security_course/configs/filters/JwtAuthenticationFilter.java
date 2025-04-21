package com.angelfg.spring_security_course.configs.filters;

import com.angelfg.spring_security_course.exceptions.ObjectNotFoundException;
import com.angelfg.spring_security_course.persistence.entities.security.JwtToken;
import com.angelfg.spring_security_course.persistence.entities.security.User;
import com.angelfg.spring_security_course.persistence.repositories.security.JwtTokenRepository;
import com.angelfg.spring_security_course.services.UserService;
import com.angelfg.spring_security_course.services.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final JwtTokenRepository jwtRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("ENTRO EN EL FILTRO JWT AUTHENTICATION FILTER");

//        // 1. Obtener encabezado http llamado Authorization
//        String authorizationHeader = request.getHeader("Authorization"); // Bearer jwt
//        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // 2. Obtener token JWT desde el encabezado
//        String jwt = authorizationHeader.split(" ")[1];

        // 1. Obtener el token
        String jwt = jwtService.extractJwtFromRequest(request);

        if (jwt == null || !StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Validar token
        Optional<JwtToken> token = jwtRepository.findByToken(jwt);
        boolean isValid = validateToken(token);

        if (!isValid) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Obtener el subject/username desde el token
        // esta accion a su vez valida el formato del token, firma y fecha de expiración
        String username = jwtService.extractUsername(jwt);

        // 4. Setear objeto authentication dentro de security context holder
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5. Ejecutar el registro de filtros
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(Optional<JwtToken> optionalJwtToken) {

        if (!optionalJwtToken.isPresent()) {
            System.out.println("Token no existe o no fue generado en nuestro sistema");
            return false;
        }

        JwtToken token = optionalJwtToken.get();
        Date now = new Date(System.currentTimeMillis()); // fecha actual
        boolean isValid = token.isValid() && token.getExpiration().after(now);

        if (!isValid) {
            System.out.println("Token inválido");
            updateTokenStatus(token);
        }

        return isValid;
    }

    private void updateTokenStatus(JwtToken token) {
        token.setValid(false);
        jwtRepository.save(token);
    }

}
