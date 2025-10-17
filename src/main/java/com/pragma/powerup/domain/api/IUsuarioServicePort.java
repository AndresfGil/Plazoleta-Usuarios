package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Usuario;

public interface IUsuarioServicePort {

    Usuario guardarUsuario(Usuario usuario);
    
    Usuario obtenerUsuarioPorId(Long id);

}
