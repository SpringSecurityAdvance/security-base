package com.angelfg.spring_security_course.persistence.entities.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"user\"") // Tabla "user" con comillas
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String name;
    private String password;

    //@Enumerated(EnumType.STRING)
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) return null;
        if (role.getPermissions() == null) return null;

        List<SimpleGrantedAuthority> authorities = role.getPermissions()
                .stream()
                //.map(each -> each.name())
                .map(each -> each.getOperation().getName())
                .map(each -> new SimpleGrantedAuthority(each))
//                .map(each -> {
//                    String permission = each.name();
//                    return new SimpleGrantedAuthority(permission);
//                })
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.getName()));

        return authorities;

// Authorities
//        return role.getPermissions()
//                .stream()
//                .map(Enum::name)
//                .map(SimpleGrantedAuthority::new)
//                .toList();

//                .map(each -> {
//                    String permission = each.name();
//                    return new SimpleGrantedAuthority(permission);
//                }).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
