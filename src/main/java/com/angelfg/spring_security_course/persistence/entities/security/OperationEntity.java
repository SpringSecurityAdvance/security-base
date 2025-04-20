package com.angelfg.spring_security_course.persistence.entities.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "operation")
@Getter
@Setter
public class OperationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String path; // /products/{productId}/disabled {PUT}
    private String httpMethod;
    private boolean permitAll;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

}
