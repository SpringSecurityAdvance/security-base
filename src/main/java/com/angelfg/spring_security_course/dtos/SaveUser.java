package com.angelfg.spring_security_course.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveUser implements Serializable {

    @Size(min = 4)
    private String name;

    private String username;

    @Size(min = 8)
    private String password;

    @Size(min = 8)
    private String repeatedPassword;

}
