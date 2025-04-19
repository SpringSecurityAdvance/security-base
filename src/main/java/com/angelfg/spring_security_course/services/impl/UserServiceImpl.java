package com.angelfg.spring_security_course.services.impl;

import com.angelfg.spring_security_course.dtos.SaveUser;
import com.angelfg.spring_security_course.exceptions.InvalidPasswordException;
import com.angelfg.spring_security_course.persistence.entities.User;
import com.angelfg.spring_security_course.persistence.enums.Role;
import com.angelfg.spring_security_course.persistence.repositories.UserRepository;
import com.angelfg.spring_security_course.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registrOneCustomer(SaveUser newUser) {
        validatePassword(newUser);

        User user = new User();
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());
        user.setRole(Role.CUSTOMER);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validatePassword(SaveUser dto) {

        if (!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getRepeatedPassword()))
            throw new InvalidPasswordException("Passwords don't match");

        if (!dto.getPassword().equals(dto.getRepeatedPassword()))
            throw new InvalidPasswordException("Passwords don't match");

    }

}
