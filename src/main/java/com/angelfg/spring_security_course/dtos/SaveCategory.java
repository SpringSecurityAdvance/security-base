package com.angelfg.spring_security_course.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveCategory implements Serializable {

    @NotBlank
    private String name;

}
