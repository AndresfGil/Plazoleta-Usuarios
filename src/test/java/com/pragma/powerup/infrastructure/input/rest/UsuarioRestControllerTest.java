package com.pragma.powerup.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.UsuarioRequestDto;
import com.pragma.powerup.application.handler.IUsuarioHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UsuarioRestController.class, 
           excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class UsuarioRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUsuarioHandler usuarioHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequestDto usuarioRequestDto;

    @BeforeEach
    void setUp() {
        usuarioRequestDto = new UsuarioRequestDto();
        usuarioRequestDto.setNombre("Juan");
        usuarioRequestDto.setApellido("Pérez");
        usuarioRequestDto.setDocumentoIdentidad(12345678);
        usuarioRequestDto.setCelular("+573001234567");
        usuarioRequestDto.setFechaNacimiento(new Date());
        usuarioRequestDto.setCorreo("juan@example.com");
        usuarioRequestDto.setClave("password123");
        usuarioRequestDto.setIdRol(4L);
    }

    @Test
    void guardarUsuario_cuandoRequestEsValido_debeRetornar201() throws Exception {
        doNothing().when(usuarioHandler).guardarUsuario(any(UsuarioRequestDto.class));

        mockMvc.perform(post("/api/v1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardarUsuario_cuandoNombreEsNull_debeRetornar400() throws Exception {
        usuarioRequestDto.setNombre(null);

        mockMvc.perform(post("/api/v1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void guardarUsuario_cuandoCorreoEsInvalido_debeRetornar400() throws Exception {
        usuarioRequestDto.setCorreo("correo-invalido");

        mockMvc.perform(post("/api/v1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void guardarUsuario_cuandoCelularEsInvalido_debeRetornar400() throws Exception {
        usuarioRequestDto.setCelular("123"); // Muy corto

        mockMvc.perform(post("/api/v1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void guardarUsuario_cuandoIdRolEsNull_debeRetornar400() throws Exception {
        usuarioRequestDto.setIdRol(null);

        mockMvc.perform(post("/api/v1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
