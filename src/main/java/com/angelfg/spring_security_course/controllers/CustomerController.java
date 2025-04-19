package com.angelfg.spring_security_course.controllers;

import com.angelfg.spring_security_course.dtos.RegisteredUser;
import com.angelfg.spring_security_course.dtos.SaveUser;
import com.angelfg.spring_security_course.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final AuthenticationService authenticationService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR', 'CUSTOMER')")
    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@Valid @RequestBody SaveUser newUser) {
        RegisteredUser registeredUser = authenticationService.registerOneCustomer(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

}
