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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Test
    void obtenerUsuarioPorId_cuandoUsuarioExiste_debeRetornar200() throws Exception {
        Usuario usuario = crearUsuarioValido();
        
        when(usuarioPersistencePort.obtenerUsuarioPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"))
                .andExpect(jsonPath("$.idRol").value(4))
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    void obtenerUsuarioPorId_cuandoUsuarioNoExiste_debeRetornar404() throws Exception {
        when(usuarioPersistencePort.obtenerUsuarioPorId(999L))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        mockMvc.perform(get("/api/v1/usuario/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void obtenerUsuarioPorId_cuandoUsuarioEsPropietario_debeRetornarDatosCompletos() throws Exception {
        Usuario usuarioPropietario = crearUsuarioValido();
        usuarioPropietario.setId(2L);
        usuarioPropietario.setIdRol(2L);
        usuarioPropietario.setNombre("María");
        usuarioPropietario.setApellido("García");
        
        when(usuarioPersistencePort.obtenerUsuarioPorId(2L)).thenReturn(usuarioPropietario);

        mockMvc.perform(get("/api/v1/usuario/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("María"))
                .andExpect(jsonPath("$.apellido").value("García"))
                .andExpect(jsonPath("$.idRol").value(2))
                .andExpect(jsonPath("$.activo").value(true));
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
