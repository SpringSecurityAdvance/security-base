package com.angelfg.spring_security_course.configs.handler;

import com.angelfg.spring_security_course.dtos.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

//    private final CustomAccessDeniedHandler accessDeniedHandler;
//    accessDeniedHandler.handle(request, response, new AccessDeniedException("Access Denied"));

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(authException.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("No se encontraron credenciales de autenticacion. " +
                "Por favor, inicie sesión para acceder a esta funcion.");
        apiError.setTimestamp(LocalDateTime.now());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String apiErrorAsJson = objectMapper.writeValueAsString(apiError);

        response.getWriter().write(apiErrorAsJson);
    }

}
