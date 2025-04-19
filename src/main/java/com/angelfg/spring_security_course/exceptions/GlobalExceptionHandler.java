package com.angelfg.spring_security_course.exceptions;

import com.angelfg.spring_security_course.dtos.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(Exception exception, HttpServletRequest request) {
        ApiError error = new ApiError();
        error.setMessage("Error interno en el servidor, vuelva a intentarlo");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(500);
        error.setUrl(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError();
        error.setMessage("Error: la petición enviada posee un formato incorrecto");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(400);
        error.setUrl(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
