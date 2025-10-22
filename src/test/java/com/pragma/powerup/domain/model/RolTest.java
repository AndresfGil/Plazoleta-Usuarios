package com.pragma.powerup.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
    }

    @Test
    void constructor_porDefecto_debeCrearRolVacio() {
        Rol nuevoRol = new Rol();

        assertNotNull(nuevoRol);
        assertNull(nuevoRol.getId());
        assertNull(nuevoRol.getNombreRol());
    }

    @Test
    void constructor_conParametros_debeCrearRolCompleto() {
        Rol rolCompleto = new Rol(1L, "ADMINISTRADOR");

        assertNotNull(rolCompleto);
        assertEquals(1L, rolCompleto.getId());
        assertEquals("ADMINISTRADOR", rolCompleto.getNombreRol());
    }

    @Test
    void setId_cuandoIdEsValido_debeEstablecerId() {
        Long id = 1L;
        rol.setId(id);

        assertEquals(id, rol.getId());
    }

    @Test
    void setId_cuandoIdEsNull_debeEstablecerNull() {
        rol.setId(null);

        assertNull(rol.getId());
    }

    @Test
    void setNombreRol_cuandoNombreEsValido_debeEstablecerNombre() {
        String nombre = "ADMINISTRADOR";
        rol.setNombreRol(nombre);

        assertEquals(nombre, rol.getNombreRol());
    }

    @Test
    void setNombreRol_cuandoNombreEsNull_debeEstablecerNull() {
        rol.setNombreRol(null);

        assertNull(rol.getNombreRol());
    }

    @Test
    void gettersYsetters_cuandoTodosLosCamposSonValidos_debeFuncionarCorrectamente() {
        Long id = 1L;
        String nombreRol = "ADMINISTRADOR";

        rol.setId(id);
        rol.setNombreRol(nombreRol);

        assertEquals(id, rol.getId());
        assertEquals(nombreRol, rol.getNombreRol());
    }

    @Test
    void constructor_conParametrosNulos_debeCrearRolConValoresNulos() {
        Rol rolNulo = new Rol(null, null);

        assertNull(rolNulo.getId());
        assertNull(rolNulo.getNombreRol());
    }

    @Test
    void setNombreRol_cuandoNombreEsVacio_debeEstablecerVacio() {
        rol.setNombreRol("");

        assertEquals("", rol.getNombreRol());
    }

    @Test
    void setId_cuandoIdEsMaximo_debeEstablecerId() {
        rol.setId(Long.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, rol.getId());
    }

    @Test
    void setId_cuandoIdEsMinimo_debeEstablecerId() {
        rol.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, rol.getId());
    }

    @Test
    void setNombreRol_cuandoNombreEsPropietario_debeEstablecerPropietario() {
        rol.setNombreRol("PROPIETARIO");

        assertEquals("PROPIETARIO", rol.getNombreRol());
    }

    @Test
    void setNombreRol_cuandoNombreEsEmpleado_debeEstablecerEmpleado() {
        rol.setNombreRol("EMPLEADO");

        assertEquals("EMPLEADO", rol.getNombreRol());
    }

    @Test
    void setNombreRol_cuandoNombreEsCliente_debeEstablecerCliente() {
        rol.setNombreRol("CLIENTE");

        assertEquals("CLIENTE", rol.getNombreRol());
    }

    @Test
    void setNombreRol_cuandoNombreEsLargo_debeEstablecerNombre() {
        String nombreLargo = "A".repeat(500);
        rol.setNombreRol(nombreLargo);

        assertEquals(nombreLargo, rol.getNombreRol());
    }

    @Test
    void setId_cuandoIdEsCero_debeEstablecerCero() {
        rol.setId(0L);

        assertEquals(0L, rol.getId());
    }
}

