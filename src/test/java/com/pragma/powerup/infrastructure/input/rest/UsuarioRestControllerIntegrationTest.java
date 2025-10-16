package com.pragma.powerup.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.UsuarioRequestDto;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUsuarioPersistencePort;
import com.pragma.powerup.domain.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import com.pragma.powerup.infrastructure.configuration.TestSecurityConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestSecurityConfiguration.class)
class UsuarioRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUsuarioPersistencePort usuarioPersistencePort;

    @MockBean
    private IPasswordEncoderPort passwordEncoderPort;

    @Test
    void guardarUsuario_integracionCompleta_debeRetornar201() throws Exception {
        UsuarioRequestDto request = crearUsuarioRequestValido();
        Usuario usuario = crearUsuarioValido();
        
        when(passwordEncoderPort.encode(anyString())).thenReturn("$2a$10$encryptedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/v1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    private UsuarioRequestDto crearUsuarioRequestValido() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -20);
        
        UsuarioRequestDto request = new UsuarioRequestDto();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setDocumentoIdentidad(12345678);
        request.setCelular("+573001234567");
        request.setFechaNacimiento(calendar.getTime());
        request.setCorreo("juan@example.com");
        request.setClave("password123");
        request.setIdRol(4L);
        return request;
    }

    private Usuario crearUsuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setDocumentoIdentidad(12345678);
        usuario.setCelular("+573001234567");
        usuario.setCorreo("juan@example.com");
        usuario.setClave("$2a$10$encryptedPassword");
        usuario.setIdRol(4L);
        usuario.setActivo(true);
        return usuario;
    }
}
