package com.angelfg.spring_security_course.persistence.entities.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class JwtToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String token;

    private Date expiration;
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
