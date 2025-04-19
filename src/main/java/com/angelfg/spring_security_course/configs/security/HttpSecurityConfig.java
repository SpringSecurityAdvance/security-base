package com.angelfg.spring_security_course.configs.security;

import com.angelfg.spring_security_course.configs.filters.JwtAuthenticationFilter;
import com.angelfg.spring_security_course.persistence.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class HttpSecurityConfig {

    private final AuthenticationProvider daoAuthProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sessMagConfig -> sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthProvider) // Estrategia de autenticacion del provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(HttpSecurityConfig::buildRequestMatchers)
                .build();
    }

    private static void buildRequestMatchers(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig
    ) {
        /*
        * Autorizacion de endpoints de products
        */
        authReqConfig.requestMatchers(HttpMethod.GET, "/products")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.READ_ALL_PRODUCTS.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/products/{productId}")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                // .hasAuthority(RolePermission.READ_ONE_PRODUCT.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/products")
                .hasAnyRole(Role.ADMINISTRATOR.name());
                // .hasAuthority(RolePermission.CREATE_ONE_PRODUCT.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                // .hasAuthority(RolePermission.UPDATE_ONE_PRODUCT.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disabled")
                .hasAnyRole(Role.ADMINISTRATOR.name());
                // .hasAuthority(RolePermission.DISABLE_ONE_PRODUCT.name());

        /*
        * Autorizacion de endpoints de categories
        */
        authReqConfig.requestMatchers(HttpMethod.GET, "/categories")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        // .hasAuthority(RolePermission.READ_ALL_CATEGORIES.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        // .hasAuthority(RolePermission.READ_ONE_CATEGORY.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/categories")
                .hasAnyRole(Role.ADMINISTRATOR.name());
        // .hasAuthority(RolePermission.CREATE_ONE_CATEGORY.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        // .hasAuthority(RolePermission.UPDATE_ONE_CATEGORY.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled")
                .hasAnyRole(Role.ADMINISTRATOR.name());
        // .hasAuthority(RolePermission.DISABLE_ONE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name(), Role.CUSTOMER.name());
        // .hasAuthority(RolePermission.READ_MY_PROFILE.name());

        /*
        * Autorizacion de endpoints publicos
        */
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        authReqConfig.anyRequest().authenticated();
    }

}
