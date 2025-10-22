package com.pragma.powerup.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioNoEncontradoExceptionTest {

    @Test
    void constructor_conMensaje_debeCrearExcepcionConMensaje() {
        String mensaje = "Usuario no encontrado con ID: 1";

        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conMensajeNull_debeCrearExcepcionConMensajeNull() {
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void constructor_conMensajeVacio_debeCrearExcepcionConMensajeVacio() {
        String mensaje = "";

        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void exception_debeSerRuntimeException() {
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException("Error");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void getMessage_cuandoSeEstableceUnMensaje_debeRetornarMensaje() {
        String mensaje = "Usuario no encontrado";

        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(mensaje);

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conMensajeLargo_debeCrearExcepcionConMensajeLargo() {
        String mensajeLargo = "Usuario no encontrado: " + "A".repeat(1000);

        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(mensajeLargo);

        assertEquals(mensajeLargo, exception.getMessage());
    }

    @Test
    void exception_cuandoSeLanza_debePropagarse() {
        String mensaje = "Usuario no encontrado";

        assertThrows(UsuarioNoEncontradoException.class, () -> {
            throw new UsuarioNoEncontradoException(mensaje);
        });
    }

    @Test
    void exception_cuandoSeLanza_debeTenerMensajeCorrecto() {
        String mensaje = "Usuario no encontrado con ID: 999";

        UsuarioNoEncontradoException exception = assertThrows(UsuarioNoEncontradoException.class, () -> {
            throw new UsuarioNoEncontradoException(mensaje);
        });

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conDiferentesIds_debeCrearExcepcion() {
        UsuarioNoEncontradoException exception1 = new UsuarioNoEncontradoException("Usuario no encontrado con ID: 1");
        UsuarioNoEncontradoException exception2 = new UsuarioNoEncontradoException("Usuario no encontrado con ID: 2");
        UsuarioNoEncontradoException exception3 = new UsuarioNoEncontradoException("Usuario no encontrado con ID: 999");

        assertEquals("Usuario no encontrado con ID: 1", exception1.getMessage());
        assertEquals("Usuario no encontrado con ID: 2", exception2.getMessage());
        assertEquals("Usuario no encontrado con ID: 999", exception3.getMessage());
    }

    @Test
    void exception_debePoderSerCapturada() {
        String mensaje = "Usuario no encontrado";

        try {
            throw new UsuarioNoEncontradoException(mensaje);
        } catch (UsuarioNoEncontradoException e) {
            assertEquals(mensaje, e.getMessage());
        }
    }

    @Test
    void exception_cuandoSeCreaConMensaje_debeSerDiferenteDeNull() {
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException("Mensaje");

        assertNotNull(exception);
        assertNotNull(exception.getMessage());
    }

    @Test
    void exception_cuandoSeCreanDosConMismomensaje_debenSerDiferentes() {
        String mensaje = "Usuario no encontrado";
        UsuarioNoEncontradoException exception1 = new UsuarioNoEncontradoException(mensaje);
        UsuarioNoEncontradoException exception2 = new UsuarioNoEncontradoException(mensaje);

        assertNotEquals(exception1, exception2);
        assertEquals(exception1.getMessage(), exception2.getMessage());
    }

    @Test
    void exception_cuandoSeCaptura_debeMantenerMensaje() {
        String mensajeOriginal = "Usuario no encontrado con correo: test@example.com";

        try {
            throw new UsuarioNoEncontradoException(mensajeOriginal);
        } catch (RuntimeException e) {
            assertTrue(e instanceof UsuarioNoEncontradoException);
            assertEquals(mensajeOriginal, e.getMessage());
        }
    }

    @Test
    void exception_cuandoSeLanzaDesdePersistence_debePropagarse() {
        String mensaje = "Usuario no encontrado en la base de datos";

        assertThrows(RuntimeException.class, () -> {
            throw new UsuarioNoEncontradoException(mensaje);
        });
    }

    @Test
    void constructor_conMensajeConCaracteresEspeciales_debeCrearExcepcion() {
        String mensaje = "Usuario no encontrado: ID=123, Correo=test@example.com, Estado=activo";

        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(mensaje);

        assertEquals(mensaje, exception.getMessage());
    }
}

