package com.angelfg.spring_security_course.services.auth;

import com.angelfg.spring_security_course.dtos.RegisteredUser;
import com.angelfg.spring_security_course.dtos.SaveUser;
import com.angelfg.spring_security_course.dtos.auth.AuthenticationRequest;
import com.angelfg.spring_security_course.dtos.auth.AuthenticationResponse;
import com.angelfg.spring_security_course.exceptions.ObjectNotFoundException;
import com.angelfg.spring_security_course.persistence.entities.security.JwtToken;
import com.angelfg.spring_security_course.persistence.entities.security.User;
import com.angelfg.spring_security_course.persistence.repositories.security.JwtTokenRepository;
import com.angelfg.spring_security_course.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRepository jwtRepository;

    public RegisteredUser registerOneCustomer(SaveUser newUser) {
        User user = userService.registrOneCustomer(newUser);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveUserToken(user, jwt);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName());
        userDto.setJwt(jwt);

        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    public AuthenticationResponse login(AuthenticationRequest autRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                autRequest.getUsername(),
                autRequest.getPassword()
        );

        authenticationManager.authenticate(authentication);

        UserDetails user = userService.findOneByUsername(autRequest.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));
        saveUserToken((User) user, jwt);

        AuthenticationResponse authRsp = new AuthenticationResponse();
        authRsp.setJwt(jwt);

        return authRsp;
    }

    public boolean validateToken(String jwt) {
        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User findLoggedInUser() {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String username = (String) auth.getPrincipal();

        return userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));
    }

    private void saveUserToken(User user, String jwt) {

        JwtToken token = new JwtToken();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiration(jwtService.extractExpiration(jwt));
        token.setValid(true);

        jwtRepository.save(token);
    }

    public void logout(HttpServletRequest request) {
        String jwt = jwtService.extractJwtFromRequest(request);

        if (jwt == null || !StringUtils.hasText(jwt)) return;

        Optional<JwtToken> token = jwtRepository.findByToken(jwt);

        if (token.isPresent() && token.get().isValid()) {
            token.get().setValid(false);
            jwtRepository.save(token.get());
        }

    }

}
