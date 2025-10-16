package com.pragma.powerup.infrastructure.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * Configuración de seguridad para tests
 * Deshabilita la seguridad para permitir tests sin autenticación
 */
@TestConfiguration
public class TestSecurityConfiguration {

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Sin encriptación para tests
    }
}
