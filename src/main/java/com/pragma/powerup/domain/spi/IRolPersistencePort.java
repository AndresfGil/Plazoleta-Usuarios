package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Rol;

public interface IRolPersistencePort {

    Rol obtenerRolPorId(Long id);
}
