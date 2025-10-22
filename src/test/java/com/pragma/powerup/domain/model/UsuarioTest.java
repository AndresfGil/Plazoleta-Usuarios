package com.pragma.powerup.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    void constructor_porDefecto_debeCrearUsuarioVacio() {
        Usuario nuevoUsuario = new Usuario();

        assertNotNull(nuevoUsuario);
        assertNull(nuevoUsuario.getId());
        assertNull(nuevoUsuario.getNombre());
        assertNull(nuevoUsuario.getApellido());
        assertNull(nuevoUsuario.getDocumentoIdentidad());
        assertNull(nuevoUsuario.getCelular());
        assertNull(nuevoUsuario.getFechaNacimiento());
        assertNull(nuevoUsuario.getCorreo());
        assertNull(nuevoUsuario.getClave());
        assertNull(nuevoUsuario.getIdRol());
        assertNull(nuevoUsuario.getActivo());
    }

    @Test
    void constructor_conParametros_debeCrearUsuarioCompleto() {
        Date fecha = new Date();
        Usuario usuarioCompleto = new Usuario(1L, "Juan", "Perez", 123456789, "+573001234567", 
                fecha, "juan@example.com", "password123", 2L, true);

        assertNotNull(usuarioCompleto);
        assertEquals(1L, usuarioCompleto.getId());
        assertEquals("Juan", usuarioCompleto.getNombre());
        assertEquals("Perez", usuarioCompleto.getApellido());
        assertEquals(123456789, usuarioCompleto.getDocumentoIdentidad());
        assertEquals("+573001234567", usuarioCompleto.getCelular());
        assertEquals(fecha, usuarioCompleto.getFechaNacimiento());
        assertEquals("juan@example.com", usuarioCompleto.getCorreo());
        assertEquals("password123", usuarioCompleto.getClave());
        assertEquals(2L, usuarioCompleto.getIdRol());
        assertTrue(usuarioCompleto.getActivo());
    }

    @Test
    void setId_cuandoIdEsValido_debeEstablecerId() {
        Long id = 1L;
        usuario.setId(id);

        assertEquals(id, usuario.getId());
    }

    @Test
    void setId_cuandoIdEsNull_debeEstablecerNull() {
        usuario.setId(null);

        assertNull(usuario.getId());
    }

    @Test
    void setNombre_cuandoNombreEsValido_debeEstablecerNombre() {
        String nombre = "Juan";
        usuario.setNombre(nombre);

        assertEquals(nombre, usuario.getNombre());
    }

    @Test
    void setNombre_cuandoNombreEsNull_debeEstablecerNull() {
        usuario.setNombre(null);

        assertNull(usuario.getNombre());
    }

    @Test
    void setApellido_cuandoApellidoEsValido_debeEstablecerApellido() {
        String apellido = "Perez";
        usuario.setApellido(apellido);

        assertEquals(apellido, usuario.getApellido());
    }

    @Test
    void setApellido_cuandoApellidoEsNull_debeEstablecerNull() {
        usuario.setApellido(null);

        assertNull(usuario.getApellido());
    }

    @Test
    void setDocumentoIdentidad_cuandoDocumentoEsValido_debeEstablecerDocumento() {
        Integer documento = 123456789;
        usuario.setDocumentoIdentidad(documento);

        assertEquals(documento, usuario.getDocumentoIdentidad());
    }

    @Test
    void setDocumentoIdentidad_cuandoDocumentoEsNull_debeEstablecerNull() {
        usuario.setDocumentoIdentidad(null);

        assertNull(usuario.getDocumentoIdentidad());
    }

    @Test
    void setCelular_cuandoCelularEsValido_debeEstablecerCelular() {
        String celular = "+573001234567";
        usuario.setCelular(celular);

        assertEquals(celular, usuario.getCelular());
    }

    @Test
    void setCelular_cuandoCelularEsNull_debeEstablecerNull() {
        usuario.setCelular(null);

        assertNull(usuario.getCelular());
    }

    @Test
    void setFechaNacimiento_cuandoFechaEsValida_debeEstablecerFecha() {
        Date fecha = new Date();
        usuario.setFechaNacimiento(fecha);

        assertEquals(fecha, usuario.getFechaNacimiento());
    }

    @Test
    void setFechaNacimiento_cuandoFechaEsNull_debeEstablecerNull() {
        usuario.setFechaNacimiento(null);

        assertNull(usuario.getFechaNacimiento());
    }

    @Test
    void setCorreo_cuandoCorreoEsValido_debeEstablecerCorreo() {
        String correo = "juan@example.com";
        usuario.setCorreo(correo);

        assertEquals(correo, usuario.getCorreo());
    }

    @Test
    void setCorreo_cuandoCorreoEsNull_debeEstablecerNull() {
        usuario.setCorreo(null);

        assertNull(usuario.getCorreo());
    }

    @Test
    void setClave_cuandoClaveEsValida_debeEstablecerClave() {
        String clave = "password123";
        usuario.setClave(clave);

        assertEquals(clave, usuario.getClave());
    }

    @Test
    void setClave_cuandoClaveEsNull_debeEstablecerNull() {
        usuario.setClave(null);

        assertNull(usuario.getClave());
    }

    @Test
    void setIdRol_cuandoIdRolEsValido_debeEstablecerIdRol() {
        Long idRol = 2L;
        usuario.setIdRol(idRol);

        assertEquals(idRol, usuario.getIdRol());
    }

    @Test
    void setIdRol_cuandoIdRolEsNull_debeEstablecerNull() {
        usuario.setIdRol(null);

        assertNull(usuario.getIdRol());
    }

    @Test
    void setActivo_cuandoActivoEsTrue_debeEstablecerTrue() {
        usuario.setActivo(true);

        assertTrue(usuario.getActivo());
    }

    @Test
    void setActivo_cuandoActivoEsFalse_debeEstablecerFalse() {
        usuario.setActivo(false);

        assertFalse(usuario.getActivo());
    }

    @Test
    void setActivo_cuandoActivoEsNull_debeEstablecerNull() {
        usuario.setActivo(null);

        assertNull(usuario.getActivo());
    }

    @Test
    void gettersYsetters_cuandoTodosLosCamposSonValidos_debeFuncionarCorrectamente() {
        Long id = 1L;
        String nombre = "Juan";
        String apellido = "Perez";
        Integer documento = 123456789;
        String celular = "+573001234567";
        Date fecha = new Date();
        String correo = "juan@example.com";
        String clave = "password123";
        Long idRol = 2L;
        Boolean activo = true;

        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDocumentoIdentidad(documento);
        usuario.setCelular(celular);
        usuario.setFechaNacimiento(fecha);
        usuario.setCorreo(correo);
        usuario.setClave(clave);
        usuario.setIdRol(idRol);
        usuario.setActivo(activo);

        assertEquals(id, usuario.getId());
        assertEquals(nombre, usuario.getNombre());
        assertEquals(apellido, usuario.getApellido());
        assertEquals(documento, usuario.getDocumentoIdentidad());
        assertEquals(celular, usuario.getCelular());
        assertEquals(fecha, usuario.getFechaNacimiento());
        assertEquals(correo, usuario.getCorreo());
        assertEquals(clave, usuario.getClave());
        assertEquals(idRol, usuario.getIdRol());
        assertEquals(activo, usuario.getActivo());
    }

    @Test
    void constructor_conParametrosNulos_debeCrearUsuarioConValoresNulos() {
        Usuario usuarioNulo = new Usuario(null, null, null, null, null, null, null, null, null, null);

        assertNull(usuarioNulo.getId());
        assertNull(usuarioNulo.getNombre());
        assertNull(usuarioNulo.getApellido());
        assertNull(usuarioNulo.getDocumentoIdentidad());
        assertNull(usuarioNulo.getCelular());
        assertNull(usuarioNulo.getFechaNacimiento());
        assertNull(usuarioNulo.getCorreo());
        assertNull(usuarioNulo.getClave());
        assertNull(usuarioNulo.getIdRol());
        assertNull(usuarioNulo.getActivo());
    }

    @Test
    void setNombre_cuandoNombreEsVacio_debeEstablecerVacio() {
        usuario.setNombre("");

        assertEquals("", usuario.getNombre());
    }

    @Test
    void setApellido_cuandoApellidoEsVacio_debeEstablecerVacio() {
        usuario.setApellido("");

        assertEquals("", usuario.getApellido());
    }

    @Test
    void setCorreo_cuandoCorreoEsVacio_debeEstablecerVacio() {
        usuario.setCorreo("");

        assertEquals("", usuario.getCorreo());
    }

    @Test
    void setClave_cuandoClaveEsVacia_debeEstablecerVacia() {
        usuario.setClave("");

        assertEquals("", usuario.getClave());
    }

    @Test
    void setDocumentoIdentidad_cuandoDocumentoEsCero_debeEstablecerCero() {
        usuario.setDocumentoIdentidad(0);

        assertEquals(0, usuario.getDocumentoIdentidad());
    }

    @Test
    void setDocumentoIdentidad_cuandoDocumentoEsNegativo_debeEstablecerNegativo() {
        usuario.setDocumentoIdentidad(-123);

        assertEquals(-123, usuario.getDocumentoIdentidad());
    }

    @Test
    void setDocumentoIdentidad_cuandoDocumentoEsMaximo_debeEstablecerMaximo() {
        usuario.setDocumentoIdentidad(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, usuario.getDocumentoIdentidad());
    }

    @Test
    void setIdRol_cuandoIdRolEsMaximo_debeEstablecerMaximo() {
        usuario.setIdRol(Long.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, usuario.getIdRol());
    }

    @Test
    void setId_cuandoIdEsMaximo_debeEstablecerId() {
        usuario.setId(Long.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, usuario.getId());
    }

    @Test
    void setFechaNacimiento_cuandoFechaEsPasada_debeEstablecerFecha() {
        Date fechaPasada = new Date(0);
        usuario.setFechaNacimiento(fechaPasada);

        assertEquals(fechaPasada, usuario.getFechaNacimiento());
    }

    @Test
    void setFechaNacimiento_cuandoFechaEsFutura_debeEstablecerFecha() {
        Date fechaFutura = new Date(System.currentTimeMillis() + 1000000000L);
        usuario.setFechaNacimiento(fechaFutura);

        assertEquals(fechaFutura, usuario.getFechaNacimiento());
    }

    @Test
    void setCelular_cuandoCelularEsVacio_debeEstablecerVacio() {
        usuario.setCelular("");

        assertEquals("", usuario.getCelular());
    }
}

