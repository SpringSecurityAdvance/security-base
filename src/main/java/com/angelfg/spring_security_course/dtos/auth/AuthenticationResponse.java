package com.angelfg.spring_security_course.dtos.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationResponse implements Serializable {
    private String jwt;
}
