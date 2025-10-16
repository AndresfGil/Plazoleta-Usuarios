package com.pragma.powerup.domain.util;

public enum RolEnum {
    ADMINISTRADOR(1L, "ADMINISTRADOR"),
    PROPIETARIO(2L, "PROPIETARIO"),
    EMPLEADO(3L, "EMPLEADO"),
    CLIENTE(4L, "CLIENTE");

    private final Long id;
    private final String nombre;

    RolEnum(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

}

