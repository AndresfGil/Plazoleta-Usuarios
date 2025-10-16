package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Usuario;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -20);
        
        usuarioValido = new Usuario();
        usuarioValido.setNombre("Juan");
        usuarioValido.setApellido("Pérez");
        usuarioValido.setDocumentoIdentidad(12345678);
        usuarioValido.setCelular("+573001234567");
        usuarioValido.setFechaNacimiento(calendar.getTime());
        usuarioValido.setCorreo("juan@example.com");
        usuarioValido.setClave("password123");
        usuarioValido.setIdRol(4L);
    }

    @Test
    void guardarUsuario_cuandoUsuarioEsMayorDeEdad_debeGuardarExitosamente() {
        when(passwordEncoderPort.encode(anyString())).thenReturn("$2a$10$encryptedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuarioValido);

        assertDoesNotThrow(() -> usuarioUseCase.guardarUsuario(usuarioValido));

        verify(passwordEncoderPort).encode("password123");
        verify(usuarioPersistencePort).guardarUsuario(any(Usuario.class));
        assertTrue(usuarioValido.getActivo());
    }

    @Test
    void guardarUsuario_cuandoUsuarioEsMenorDeEdad_debeLanzarExcepcion() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -17);
        usuarioValido.setFechaNacimiento(calendar.getTime());

        DomainException exception = assertThrows(DomainException.class,
            () -> usuarioUseCase.guardarUsuario(usuarioValido));
        
        assertEquals("El usuario debe ser mayor de 18 años", exception.getMessage());
        verify(passwordEncoderPort, never()).encode(anyString());
        verify(usuarioPersistencePort, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    void guardarUsuario_cuandoFechaNacimientoEsNull_debeLanzarExcepcion() {
        usuarioValido.setFechaNacimiento(null);

        DomainException exception = assertThrows(DomainException.class,
            () -> usuarioUseCase.guardarUsuario(usuarioValido));
        
        assertEquals("La fecha de nacimiento es obligatoria", exception.getMessage());
        verify(passwordEncoderPort, never()).encode(anyString());
        verify(usuarioPersistencePort, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    void guardarUsuario_cuandoUsuarioEsExactamente18Años_debeGuardarExitosamente() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18); // Exactamente 18 años
        usuarioValido.setFechaNacimiento(calendar.getTime());
        
        when(passwordEncoderPort.encode(anyString())).thenReturn("$2a$10$encryptedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuarioValido);

        assertDoesNotThrow(() -> usuarioUseCase.guardarUsuario(usuarioValido));

        verify(passwordEncoderPort).encode("password123");
        verify(usuarioPersistencePort).guardarUsuario(any(Usuario.class));
        assertTrue(usuarioValido.getActivo());
    }

    @Test
    void guardarUsuario_debeEncriptarPasswordAntesDeGuardar() {
        String passwordEncriptada = "$2a$10$N9qo8uLOickgx2ZMRZoMye";
        when(passwordEncoderPort.encode("password123")).thenReturn(passwordEncriptada);
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuarioValido);

        usuarioUseCase.guardarUsuario(usuarioValido);

        verify(passwordEncoderPort).encode("password123");
        verify(usuarioPersistencePort).guardarUsuario(argThat(usuario -> 
            passwordEncriptada.equals(usuario.getClave())
        ));
    }
}
