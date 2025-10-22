package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.Usuario;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

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

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");
        usuario.setDocumentoIdentidad(123456789);
        usuario.setCelular("+573001234567");
        usuario.setFechaNacimiento(new Date());
        usuario.setCorreo("juan@example.com");
        usuario.setClave("password123");
        usuario.setIdRol(2L);
    }

    @Test
    void guardarUsuario_cuandoUsuarioEsValido_debeGuardarUsuario() {
        String claveEncriptada = "$2a$10$encodedPassword";
        Usuario usuarioEsperado = new Usuario();
        usuarioEsperado.setId(1L);
        usuarioEsperado.setNombre("Juan");
        usuarioEsperado.setApellido("Perez");
        usuarioEsperado.setDocumentoIdentidad(123456789);
        usuarioEsperado.setCelular("+573001234567");
        usuarioEsperado.setFechaNacimiento(usuario.getFechaNacimiento());
        usuarioEsperado.setCorreo("juan@example.com");
        usuarioEsperado.setClave(claveEncriptada);
        usuarioEsperado.setIdRol(2L);
        usuarioEsperado.setActivo(true);

        when(passwordEncoderPort.encode(anyString())).thenReturn(claveEncriptada);
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuarioEsperado);

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(usuarioEsperado.getId(), resultado.getId());
        assertEquals(usuarioEsperado.getNombre(), resultado.getNombre());
        assertEquals(usuarioEsperado.getApellido(), resultado.getApellido());
        assertEquals(usuarioEsperado.getDocumentoIdentidad(), resultado.getDocumentoIdentidad());
        assertEquals(usuarioEsperado.getCelular(), resultado.getCelular());
        assertEquals(usuarioEsperado.getCorreo(), resultado.getCorreo());
        assertEquals(claveEncriptada, resultado.getClave());
        assertEquals(usuarioEsperado.getIdRol(), resultado.getIdRol());
        assertTrue(resultado.getActivo());

        verify(passwordEncoderPort).encode("password123");
        verify(usuarioPersistencePort).guardarUsuario(usuario);
    }

    @Test
    void guardarUsuario_cuandoSeGuarda_debeEncriptarClave() {
        String claveOriginal = "password123";
        String claveEncriptada = "$2a$10$encodedPassword";

        when(passwordEncoderPort.encode(claveOriginal)).thenReturn(claveEncriptada);
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioUseCase.guardarUsuario(usuario);

        verify(passwordEncoderPort).encode(claveOriginal);
        assertEquals(claveEncriptada, usuario.getClave());
    }

    @Test
    void guardarUsuario_cuandoSeGuarda_debeEstablecerActivoEnTrue() {
        when(passwordEncoderPort.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioUseCase.guardarUsuario(usuario);

        assertTrue(usuario.getActivo());
        verify(usuarioPersistencePort).guardarUsuario(usuario);
    }

    @Test
    void guardarUsuario_cuandoClaveEsVacia_debeEncriptarClaveVacia() {
        usuario.setClave("");
        String claveEncriptada = "$2a$10$encodedEmptyPassword";

        when(passwordEncoderPort.encode("")).thenReturn(claveEncriptada);
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(claveEncriptada, usuario.getClave());
        verify(passwordEncoderPort).encode("");
    }

    @Test
    void guardarUsuario_cuandoClaveEsNull_debeEncriptarNull() {
        usuario.setClave(null);

        when(passwordEncoderPort.encode(null)).thenReturn("encodedNull");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioUseCase.guardarUsuario(usuario);

        verify(passwordEncoderPort).encode(null);
    }

    @Test
    void guardarUsuario_cuandoPersistenceLanzaExcepcion_debePropagarla() {
        when(passwordEncoderPort.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class)))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> usuarioUseCase.guardarUsuario(usuario));

        verify(passwordEncoderPort).encode("password123");
        verify(usuarioPersistencePort).guardarUsuario(usuario);
    }

    @Test
    void guardarUsuario_cuandoEncoderLanzaExcepcion_debePropagarla() {
        when(passwordEncoderPort.encode(anyString()))
                .thenThrow(new RuntimeException("Error al encriptar"));

        assertThrows(RuntimeException.class, () -> usuarioUseCase.guardarUsuario(usuario));

        verify(passwordEncoderPort).encode("password123");
        verify(usuarioPersistencePort, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    void obtenerUsuarioPorId_cuandoIdEsValido_debeRetornarUsuario() {
        when(usuarioPersistencePort.obtenerUsuarioPorId(1L)).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.obtenerUsuarioPorId(1L);

        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNombre(), resultado.getNombre());
        assertEquals(usuario.getApellido(), resultado.getApellido());
        verify(usuarioPersistencePort).obtenerUsuarioPorId(1L);
    }

    @Test
    void obtenerUsuarioPorId_cuandoUsuarioNoExiste_debeRetornarNull() {
        when(usuarioPersistencePort.obtenerUsuarioPorId(999L)).thenReturn(null);

        Usuario resultado = usuarioUseCase.obtenerUsuarioPorId(999L);

        assertNull(resultado);
        verify(usuarioPersistencePort).obtenerUsuarioPorId(999L);
    }

    @Test
    void obtenerUsuarioPorId_cuandoIdEsNull_debeInvocarPersistence() {
        when(usuarioPersistencePort.obtenerUsuarioPorId(null)).thenReturn(null);

        Usuario resultado = usuarioUseCase.obtenerUsuarioPorId(null);

        assertNull(resultado);
        verify(usuarioPersistencePort).obtenerUsuarioPorId(null);
    }

    @Test
    void obtenerUsuarioPorId_cuandoPersistenceLanzaExcepcion_debePropagarla() {
        when(usuarioPersistencePort.obtenerUsuarioPorId(anyLong()))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> usuarioUseCase.obtenerUsuarioPorId(1L));

        verify(usuarioPersistencePort).obtenerUsuarioPorId(1L);
    }

    @Test
    void guardarUsuario_cuandoUsuarioTieneTodosLosCampos_debeGuardarCorrectamente() {
        Date fecha = new Date();
        usuario.setFechaNacimiento(fecha);
        String claveEncriptada = "$2a$10$encodedPassword";

        when(passwordEncoderPort.encode(anyString())).thenReturn(claveEncriptada);
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());
        assertEquals(123456789, resultado.getDocumentoIdentidad());
        assertEquals("+573001234567", resultado.getCelular());
        assertEquals(fecha, resultado.getFechaNacimiento());
        assertEquals("juan@example.com", resultado.getCorreo());
        assertEquals(2L, resultado.getIdRol());
        assertTrue(resultado.getActivo());
    }

    @Test
    void guardarUsuario_cuandoUsuarioTieneCamposNulos_debeGuardarConNulos() {
        Usuario usuarioConNulos = new Usuario();
        usuarioConNulos.setClave("password");

        when(passwordEncoderPort.encode("password")).thenReturn("encodedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuarioConNulos);

        Usuario resultado = usuarioUseCase.guardarUsuario(usuarioConNulos);

        assertNotNull(resultado);
        assertTrue(usuarioConNulos.getActivo());
        verify(passwordEncoderPort).encode("password");
        verify(usuarioPersistencePort).guardarUsuario(usuarioConNulos);
    }

    @Test
    void guardarUsuario_cuandoPersistenceRetornaNull_debeRetornarNull() {
        when(passwordEncoderPort.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(null);

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertNull(resultado);
        verify(passwordEncoderPort).encode("password123");
        verify(usuarioPersistencePort).guardarUsuario(usuario);
    }

    @Test
    void obtenerUsuarioPorId_cuandoIdEsMaximo_debeInvocarPersistence() {
        when(usuarioPersistencePort.obtenerUsuarioPorId(Long.MAX_VALUE)).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.obtenerUsuarioPorId(Long.MAX_VALUE);

        assertNotNull(resultado);
        verify(usuarioPersistencePort).obtenerUsuarioPorId(Long.MAX_VALUE);
    }

    @Test
    void obtenerUsuarioPorId_cuandoIdEsMinimo_debeInvocarPersistence() {
        when(usuarioPersistencePort.obtenerUsuarioPorId(Long.MIN_VALUE)).thenReturn(null);

        Usuario resultado = usuarioUseCase.obtenerUsuarioPorId(Long.MIN_VALUE);

        assertNull(resultado);
        verify(usuarioPersistencePort).obtenerUsuarioPorId(Long.MIN_VALUE);
    }

    @Test
    void guardarUsuario_cuandoClaveTieneLongitudMaxima_debeEncriptarCorrectamente() {
        String claveLarga = "A".repeat(1000);
        usuario.setClave(claveLarga);
        String claveEncriptada = "$2a$10$encodedLongPassword";

        when(passwordEncoderPort.encode(claveLarga)).thenReturn(claveEncriptada);
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(claveEncriptada, usuario.getClave());
        verify(passwordEncoderPort).encode(claveLarga);
    }

    @Test
    void guardarUsuario_cuandoActivoYaEraTrue_debePermaneceTrue() {
        usuario.setActivo(true);

        when(passwordEncoderPort.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.guardarUsuario(usuario);

        assertTrue(resultado.getActivo());
    }

    @Test
    void guardarUsuario_cuandoActivoEraFalse_debeCambiarATrue() {
        usuario.setActivo(false);

        when(passwordEncoderPort.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioUseCase.guardarUsuario(usuario);

        assertTrue(usuario.getActivo());
    }
}

