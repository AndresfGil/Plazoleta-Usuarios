package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.dto.response.LoginResponseDto;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Rol;
import com.pragma.powerup.domain.model.Usuario;
import com.pragma.powerup.domain.spi.IRolPersistencePort;
import com.pragma.powerup.domain.spi.IUsuarioPersistencePort;
import com.pragma.powerup.infrastructure.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private IRolPersistencePort rolPersistencePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthUseCase authUseCase;

    private LoginRequestDto loginRequest;
    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequestDto();
        loginRequest.setCorreo("juan@example.com");
        loginRequest.setContrasena("password123");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");
        usuario.setDocumentoIdentidad(123456789);
        usuario.setCelular("+573001234567");
        usuario.setFechaNacimiento(new Date());
        usuario.setCorreo("juan@example.com");
        usuario.setClave("$2a$10$encodedPassword");
        usuario.setIdRol(2L);
        usuario.setActivo(true);

        rol = new Rol();
        rol.setId(2L);
        rol.setNombreRol("PROPIETARIO");
    }

    @Test
    void login_cuandoCredencialesSonValidas_debeRetornarToken() {
        String tokenEsperado = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.token";

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(2L)).thenReturn(rol);
        when(jwtUtils.generateToken("juan@example.com", "PROPIETARIO", 1L)).thenReturn(tokenEsperado);

        LoginResponseDto resultado = authUseCase.login(loginRequest);

        assertNotNull(resultado);
        assertEquals(tokenEsperado, resultado.getToken());
        assertEquals("juan@example.com", resultado.getCorreo());
        assertEquals("PROPIETARIO", resultado.getRol());

        verify(usuarioPersistencePort).obtenerUsuarioPorCorreo("juan@example.com");
        verify(passwordEncoder).matches("password123", "$2a$10$encodedPassword");
        verify(rolPersistencePort).obtenerRolPorId(2L);
        verify(jwtUtils).generateToken("juan@example.com", "PROPIETARIO", 1L);
    }

    @Test
    void login_cuandoUsuarioNoExiste_debeLanzarExcepcion() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class, 
                () -> authUseCase.login(loginRequest));

        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(usuarioPersistencePort).obtenerUsuarioPorCorreo("juan@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(rolPersistencePort, never()).obtenerRolPorId(anyLong());
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_cuandoContrasenaEsIncorrecta_debeLanzarExcepcion() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class, 
                () -> authUseCase.login(loginRequest));

        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(usuarioPersistencePort).obtenerUsuarioPorCorreo("juan@example.com");
        verify(passwordEncoder).matches("password123", "$2a$10$encodedPassword");
        verify(rolPersistencePort, never()).obtenerRolPorId(anyLong());
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_cuandoUsuarioEstaInactivo_debeLanzarExcepcion() {
        usuario.setActivo(false);

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, 
                () -> authUseCase.login(loginRequest));

        assertEquals("Usuario inactivo", exception.getMessage());
        verify(usuarioPersistencePort).obtenerUsuarioPorCorreo("juan@example.com");
        verify(passwordEncoder).matches("password123", "$2a$10$encodedPassword");
        verify(rolPersistencePort, never()).obtenerRolPorId(anyLong());
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_cuandoUsuarioEsAdministrador_debeRetornarTokenConRolAdministrador() {
        usuario.setIdRol(1L);
        rol.setId(1L);
        rol.setNombreRol("ADMINISTRADOR");
        String tokenEsperado = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.adminToken";

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(1L)).thenReturn(rol);
        when(jwtUtils.generateToken("juan@example.com", "ADMINISTRADOR", 1L)).thenReturn(tokenEsperado);

        LoginResponseDto resultado = authUseCase.login(loginRequest);

        assertNotNull(resultado);
        assertEquals(tokenEsperado, resultado.getToken());
        assertEquals("ADMINISTRADOR", resultado.getRol());
        verify(rolPersistencePort).obtenerRolPorId(1L);
    }

    @Test
    void login_cuandoUsuarioEsEmpleado_debeRetornarTokenConRolEmpleado() {
        usuario.setIdRol(3L);
        rol.setId(3L);
        rol.setNombreRol("EMPLEADO");
        String tokenEsperado = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.empleadoToken";

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(3L)).thenReturn(rol);
        when(jwtUtils.generateToken("juan@example.com", "EMPLEADO", 1L)).thenReturn(tokenEsperado);

        LoginResponseDto resultado = authUseCase.login(loginRequest);

        assertNotNull(resultado);
        assertEquals("EMPLEADO", resultado.getRol());
        verify(rolPersistencePort).obtenerRolPorId(3L);
    }

    @Test
    void login_cuandoUsuarioEsCliente_debeRetornarTokenConRolCliente() {
        usuario.setIdRol(4L);
        rol.setId(4L);
        rol.setNombreRol("CLIENTE");
        String tokenEsperado = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.clienteToken";

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(4L)).thenReturn(rol);
        when(jwtUtils.generateToken("juan@example.com", "CLIENTE", 1L)).thenReturn(tokenEsperado);

        LoginResponseDto resultado = authUseCase.login(loginRequest);

        assertNotNull(resultado);
        assertEquals("CLIENTE", resultado.getRol());
        verify(rolPersistencePort).obtenerRolPorId(4L);
    }

    @Test
    void login_cuandoCorreoEsDiferente_debeInvocarConCorreoCorrecto() {
        loginRequest.setCorreo("maria@example.com");
        usuario.setCorreo("maria@example.com");

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("maria@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(2L)).thenReturn(rol);
        when(jwtUtils.generateToken("maria@example.com", "PROPIETARIO", 1L)).thenReturn("token");

        LoginResponseDto resultado = authUseCase.login(loginRequest);

        assertEquals("maria@example.com", resultado.getCorreo());
        verify(usuarioPersistencePort).obtenerUsuarioPorCorreo("maria@example.com");
    }

    @Test
    void login_cuandoContrasenaEsDiferente_debeValidarCorrectamente() {
        loginRequest.setContrasena("otherPassword");

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("otherPassword", "$2a$10$encodedPassword")).thenReturn(false);

        assertThrows(DomainException.class, () -> authUseCase.login(loginRequest));

        verify(passwordEncoder).matches("otherPassword", "$2a$10$encodedPassword");
    }

    @Test
    void login_cuandoPersistenceLanzaExcepcion_debePropagarla() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com"))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> authUseCase.login(loginRequest));

        verify(usuarioPersistencePort).obtenerUsuarioPorCorreo("juan@example.com");
    }

    @Test
    void login_cuandoRolPersistenceLanzaExcepcion_debePropagarla() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(2L))
                .thenThrow(new RuntimeException("Error al obtener rol"));

        assertThrows(RuntimeException.class, () -> authUseCase.login(loginRequest));

        verify(rolPersistencePort).obtenerRolPorId(2L);
    }

    @Test
    void login_cuandoJwtUtilsLanzaExcepcion_debePropagarla() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(2L)).thenReturn(rol);
        when(jwtUtils.generateToken("juan@example.com", "PROPIETARIO", 1L))
                .thenThrow(new RuntimeException("Error al generar token"));

        assertThrows(RuntimeException.class, () -> authUseCase.login(loginRequest));

        verify(jwtUtils).generateToken("juan@example.com", "PROPIETARIO", 1L);
    }

    @Test
    void login_cuandoPasswordEncoderLanzaExcepcion_debePropagarla() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword"))
                .thenThrow(new RuntimeException("Error al validar password"));

        assertThrows(RuntimeException.class, () -> authUseCase.login(loginRequest));

        verify(passwordEncoder).matches("password123", "$2a$10$encodedPassword");
    }

    @Test
    void login_cuandoUsuarioActivoEsNull_debeLanzarExcepcion() {
        usuario.setActivo(null);

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);

        assertThrows(NullPointerException.class, () -> authUseCase.login(loginRequest));

        verify(usuarioPersistencePort).obtenerUsuarioPorCorreo("juan@example.com");
        verify(passwordEncoder).matches("password123", "$2a$10$encodedPassword");
    }

    @Test
    void login_cuandoCorreoEsVacio_debeInvocarPersistence() {
        loginRequest.setCorreo("");

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("")).thenReturn(null);

        assertThrows(DomainException.class, () -> authUseCase.login(loginRequest));

        verify(usuarioPersistencePort).obtenerUsuarioPorCorreo("");
    }

    @Test
    void login_cuandoContrasenaEsVacia_debeValidarCorrectamente() {
        loginRequest.setContrasena("");

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("", "$2a$10$encodedPassword")).thenReturn(false);

        assertThrows(DomainException.class, () -> authUseCase.login(loginRequest));

        verify(passwordEncoder).matches("", "$2a$10$encodedPassword");
    }

    @Test
    void login_cuandoRolEsNull_debeLanzarExcepcion() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(2L)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> authUseCase.login(loginRequest));

        verify(rolPersistencePort).obtenerRolPorId(2L);
    }

    @Test
    void login_cuandoTodoEsValido_debeGenerarTokenConDatosCorrectos() {
        String tokenEsperado = "validToken";

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(2L)).thenReturn(rol);
        when(jwtUtils.generateToken("juan@example.com", "PROPIETARIO", 1L)).thenReturn(tokenEsperado);

        LoginResponseDto resultado = authUseCase.login(loginRequest);

        assertNotNull(resultado);
        assertNotNull(resultado.getToken());
        assertNotNull(resultado.getCorreo());
        assertNotNull(resultado.getRol());
        verify(jwtUtils).generateToken("juan@example.com", "PROPIETARIO", 1L);
    }

    @Test
    void login_cuandoIdUsuarioEsDiferente_debeGenerarTokenConIdCorrecto() {
        usuario.setId(999L);
        String tokenEsperado = "tokenWithId999";

        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("juan@example.com")).thenReturn(usuario);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(rolPersistencePort.obtenerRolPorId(2L)).thenReturn(rol);
        when(jwtUtils.generateToken("juan@example.com", "PROPIETARIO", 999L)).thenReturn(tokenEsperado);

        LoginResponseDto resultado = authUseCase.login(loginRequest);

        assertEquals(tokenEsperado, resultado.getToken());
        verify(jwtUtils).generateToken("juan@example.com", "PROPIETARIO", 999L);
    }
}

