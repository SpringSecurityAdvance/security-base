package com.angelfg.spring_security_course.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

// Soluciona problema en pageable

// Cambia los el tipo de paginacion y mas detalles en las properties
// https://docs.spring.io/spring-boot/appendix/application-properties/index.html
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig {}
