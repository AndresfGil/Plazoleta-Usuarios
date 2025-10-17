package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Usuario;
import com.pragma.powerup.infrastructure.out.jpa.entity.UsuarioEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IUsuarioEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioJpaAdapterTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private IUsuarioEntityMapper usuarioEntityMapper;

    @InjectMocks
    private UsuarioJpaAdapter usuarioJpaAdapter;

    private Usuario usuario;
    private UsuarioEntity usuarioEntity;
    private UsuarioEntity usuarioEntityGuardado;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setDocumentoIdentidad(12345678);
        usuario.setCelular("+573001234567");
        usuario.setCorreo("juan@example.com");
        usuario.setClave("$2a$10$encryptedPassword");
        usuario.setIdRol(4L);
        usuario.setActivo(true);

        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNombre("Juan");
        usuarioEntity.setApellido("Pérez");
        usuarioEntity.setDocumentoIdentidad(12345678);
        usuarioEntity.setCelular("+573001234567");
        usuarioEntity.setCorreo("juan@example.com");
        usuarioEntity.setClave("$2a$10$encryptedPassword");
        usuarioEntity.setActivo(true);

        usuarioEntityGuardado = new UsuarioEntity();
        usuarioEntityGuardado.setId(1L);
        usuarioEntityGuardado.setNombre("Juan");
        usuarioEntityGuardado.setApellido("Pérez");
        usuarioEntityGuardado.setDocumentoIdentidad(12345678);
        usuarioEntityGuardado.setCelular("+573001234567");
        usuarioEntityGuardado.setCorreo("juan@example.com");
        usuarioEntityGuardado.setClave("$2a$10$encryptedPassword");
        usuarioEntityGuardado.setActivo(true);
    }

    @Test
    void guardarUsuario_cuandoUsuarioEsValido_debeRetornarUsuarioConId() {
        when(usuarioEntityMapper.toEntity(usuario)).thenReturn(usuarioEntity);
        when(usuarioRepository.save(usuarioEntity)).thenReturn(usuarioEntityGuardado);
        when(usuarioEntityMapper.toUsuario(usuarioEntityGuardado)).thenReturn(usuario);

        Usuario resultado = usuarioJpaAdapter.guardarUsuario(usuario);

        assertNotNull(resultado);
        verify(usuarioEntityMapper).toEntity(usuario);
        verify(usuarioRepository).save(usuarioEntity);
        verify(usuarioEntityMapper).toUsuario(usuarioEntityGuardado);
    }

    @Test
    void guardarUsuario_cuandoMapperRetornaNull_debeLlamarRepositoryConNull() {
        when(usuarioEntityMapper.toEntity(usuario)).thenReturn(null);
        when(usuarioRepository.save(null)).thenReturn(null);
        when(usuarioEntityMapper.toUsuario(null)).thenReturn(null);

        Usuario resultado = usuarioJpaAdapter.guardarUsuario(usuario);

        assertNull(resultado);
        verify(usuarioEntityMapper).toEntity(usuario);
        verify(usuarioRepository).save(null);
        verify(usuarioEntityMapper).toUsuario(null);
    }

    @Test
    void guardarUsuario_debeMapearCorrectamenteLosCampos() {
        when(usuarioEntityMapper.toEntity(any(Usuario.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntityGuardado);
        when(usuarioEntityMapper.toUsuario(any(UsuarioEntity.class))).thenReturn(usuario);

        usuarioJpaAdapter.guardarUsuario(usuario);

        verify(usuarioEntityMapper).toEntity(argThat(u ->
            "Juan".equals(u.getNombre()) &&
            "Pérez".equals(u.getApellido()) &&
            Integer.valueOf(12345678).equals(u.getDocumentoIdentidad()) &&
            "+573001234567".equals(u.getCelular()) &&
            "juan@example.com".equals(u.getCorreo()) &&
            "$2a$10$encryptedPassword".equals(u.getClave()) &&
            Long.valueOf(4L).equals(u.getIdRol()) &&
            Boolean.TRUE.equals(u.getActivo())
        ));
    }
}

