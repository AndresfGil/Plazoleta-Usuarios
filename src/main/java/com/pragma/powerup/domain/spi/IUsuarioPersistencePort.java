package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Usuario;


public interface IUsuarioPersistencePort {

    Usuario guardarUsuario(Usuario usuario);
    
    Usuario obtenerUsuarioPorId(Long id);
}
