package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Usuario;

/**
 * Puerto de salida (SPI) para persistencia de Usuario
 */
public interface IUsuarioPersistencePort {

    Usuario guardarUsuario(Usuario usuario);
}
