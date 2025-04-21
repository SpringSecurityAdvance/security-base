package com.angelfg.spring_security_course.persistence.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore // quitar el stackoverflow
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<GrantedPermission> permissions = new ArrayList<>();

}
