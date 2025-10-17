package com.pragma.powerup.infrastructure.out.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordEncoderAdapter passwordEncoderAdapter;

    private String passwordPlano;
    private String passwordEncriptado;

    @BeforeEach
    void setUp() {
        passwordPlano = "password123";
        passwordEncriptado = "$2a$10$N9qo8uLOickgx2ZMRZoMye/IbiMwbJvJJ9uK8xTKzQy.Zc3QVRDqe";
    }

    @Test
    void encode_cuandoPasswordEsValido_debeRetornarPasswordEncriptado() {
        when(passwordEncoder.encode(passwordPlano)).thenReturn(passwordEncriptado);

        String resultado = passwordEncoderAdapter.encode(passwordPlano);

        assertEquals(passwordEncriptado, resultado);
        verify(passwordEncoder).encode(passwordPlano);
    }

    @Test
    void encode_cuandoPasswordEsNull_debeLlamarEncoderConNull() {
        when(passwordEncoder.encode(null)).thenReturn(null);

        String resultado = passwordEncoderAdapter.encode(null);

        assertNull(resultado);
        verify(passwordEncoder).encode(null);
    }

    @Test
    void encode_cuandoPasswordEsVacio_debeLlamarEncoderConStringVacio() {
        String passwordVacio = "";
        when(passwordEncoder.encode(passwordVacio)).thenReturn("$2a$10$empty");

        String resultado = passwordEncoderAdapter.encode(passwordVacio);

        assertEquals("$2a$10$empty", resultado);
        verify(passwordEncoder).encode(passwordVacio);
    }

    @Test
    void encode_debeLlamarPasswordEncoderUnaSolaVez() {
        when(passwordEncoder.encode(anyString())).thenReturn(passwordEncriptado);

        passwordEncoderAdapter.encode(passwordPlano);

        verify(passwordEncoder, times(1)).encode(passwordPlano);
    }
}

