package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.UsuarioRequestDto;
import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.application.mapper.IUsuarioRequestMapper;
import com.pragma.powerup.application.mapper.IUsuarioResponseMapper;
import com.pragma.powerup.domain.api.IUsuarioServicePort;
import com.pragma.powerup.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioHandlerTest {

    @Mock
    private IUsuarioServicePort usuarioServicePort;

    @Mock
    private IUsuarioRequestMapper usuarioRequestMapper;

    @Mock
    private IUsuarioResponseMapper usuarioResponseMapper;

    @InjectMocks
    private UsuarioHandler usuarioHandler;

    private UsuarioRequestDto usuarioRequestDto;
    private Usuario usuario;
    private UsuarioResponseDto usuarioResponseDto;

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

        usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setDocumentoIdentidad(12345678);
        usuario.setCelular("+573001234567");
        usuario.setFechaNacimiento(new Date());
        usuario.setCorreo("juan@example.com");
        usuario.setClave("password123");
        usuario.setIdRol(4L);

        usuarioResponseDto = new UsuarioResponseDto();
        usuarioResponseDto.setId(1L);
        usuarioResponseDto.setNombre("Juan");
        usuarioResponseDto.setApellido("Pérez");
        usuarioResponseDto.setIdRol(4L);
        usuarioResponseDto.setActivo(true);
    }

    @Test
    void guardarUsuario_cuandoRequestEsValido_debeLlamarServicePort() {
        when(usuarioRequestMapper.toUsuario(usuarioRequestDto)).thenReturn(usuario);

        usuarioHandler.guardarUsuario(usuarioRequestDto);

        verify(usuarioRequestMapper).toUsuario(usuarioRequestDto);
        verify(usuarioServicePort).guardarUsuario(usuario);
    }

    @Test
    void guardarUsuario_cuandoMapperRetornaNull_debeLlamarServicePortConNull() {
        when(usuarioRequestMapper.toUsuario(usuarioRequestDto)).thenReturn(null);

        usuarioHandler.guardarUsuario(usuarioRequestDto);

        verify(usuarioRequestMapper).toUsuario(usuarioRequestDto);
        verify(usuarioServicePort).guardarUsuario(null);
    }

    @Test
    void guardarUsuario_debeMapearCorrectamenteLosCampos() {
        when(usuarioRequestMapper.toUsuario(any(UsuarioRequestDto.class))).thenReturn(usuario);

        usuarioHandler.guardarUsuario(usuarioRequestDto);

        verify(usuarioRequestMapper).toUsuario(argThat(dto ->
            "Juan".equals(dto.getNombre()) &&
            "Pérez".equals(dto.getApellido()) &&
            Integer.valueOf(12345678).equals(dto.getDocumentoIdentidad()) &&
            "+573001234567".equals(dto.getCelular()) &&
            "juan@example.com".equals(dto.getCorreo()) &&
            "password123".equals(dto.getClave()) &&
            Long.valueOf(4L).equals(dto.getIdRol())
        ));
    }

    @Test
    void obtenerUsuarioPorId_cuandoUsuarioExiste_debeRetornarResponseDto() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuario);
        when(usuarioResponseMapper.toResponse(usuario)).thenReturn(usuarioResponseDto);

        UsuarioResponseDto resultado = usuarioHandler.obtenerUsuarioPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Pérez", resultado.getApellido());
        assertEquals(4L, resultado.getIdRol());
        assertTrue(resultado.getActivo());

        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(usuarioResponseMapper).toResponse(usuario);
    }

    @Test
    void obtenerUsuarioPorId_cuandoUsuarioNoExiste_debeLanzarExcepcion() {
        when(usuarioServicePort.obtenerUsuarioPorId(999L))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        assertThrows(RuntimeException.class, () -> usuarioHandler.obtenerUsuarioPorId(999L));

        verify(usuarioServicePort).obtenerUsuarioPorId(999L);
        verify(usuarioResponseMapper, never()).toResponse(any());
    }

    @Test
    void obtenerUsuarioPorId_cuandoMapperRetornaNull_debeRetornarNull() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuario);
        when(usuarioResponseMapper.toResponse(usuario)).thenReturn(null);

        UsuarioResponseDto resultado = usuarioHandler.obtenerUsuarioPorId(1L);

        assertNull(resultado);

        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(usuarioResponseMapper).toResponse(usuario);
    }
}

