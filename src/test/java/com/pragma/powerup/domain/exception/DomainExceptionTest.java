package com.pragma.powerup.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void constructor_conMensaje_debeCrearExcepcionConMensaje() {
        String mensaje = "Error en el dominio";

        DomainException exception = new DomainException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conMensajeNull_debeCrearExcepcionConMensajeNull() {
        DomainException exception = new DomainException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void constructor_conMensajeVacio_debeCrearExcepcionConMensajeVacio() {
        String mensaje = "";

        DomainException exception = new DomainException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void exception_debeSerRuntimeException() {
        DomainException exception = new DomainException("Error");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void getMessage_cuandoSeEstableceUnMensaje_debeRetornarMensaje() {
        String mensaje = "Credenciales inválidas";

        DomainException exception = new DomainException(mensaje);

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conMensajeLargo_debeCrearExcepcionConMensajeLargo() {
        String mensajeLargo = "A".repeat(1000);

        DomainException exception = new DomainException(mensajeLargo);

        assertEquals(mensajeLargo, exception.getMessage());
    }

    @Test
    void exception_cuandoSeLanza_debePropagarse() {
        String mensaje = "Error de prueba";

        assertThrows(DomainException.class, () -> {
            throw new DomainException(mensaje);
        });
    }

    @Test
    void exception_cuandoSeLanza_debeTenerMensajeCorrecto() {
        String mensaje = "Error de validación";

        DomainException exception = assertThrows(DomainException.class, () -> {
            throw new DomainException(mensaje);
        });

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conMensajeEspecifico_debeCrearExcepcion() {
        DomainException exception1 = new DomainException("Usuario no encontrado");
        DomainException exception2 = new DomainException("Credenciales inválidas");
        DomainException exception3 = new DomainException("Usuario inactivo");

        assertEquals("Usuario no encontrado", exception1.getMessage());
        assertEquals("Credenciales inválidas", exception2.getMessage());
        assertEquals("Usuario inactivo", exception3.getMessage());
    }

    @Test
    void exception_debePoderSerCapturada() {
        String mensaje = "Error capturado";

        try {
            throw new DomainException(mensaje);
        } catch (DomainException e) {
            assertEquals(mensaje, e.getMessage());
        }
    }

    @Test
    void exception_cuandoSeCreaConMensaje_debeSerDiferenteDeNull() {
        DomainException exception = new DomainException("Mensaje");

        assertNotNull(exception);
        assertNotNull(exception.getMessage());
    }

    @Test
    void exception_cuandoSeCreanDosConMismomensaje_debenSerDiferentes() {
        String mensaje = "Mismo mensaje";
        DomainException exception1 = new DomainException(mensaje);
        DomainException exception2 = new DomainException(mensaje);

        assertNotEquals(exception1, exception2);
        assertEquals(exception1.getMessage(), exception2.getMessage());
    }
}

