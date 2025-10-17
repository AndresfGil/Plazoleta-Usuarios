package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Usuario;

/**
 * Puerto de entrada (API) para el servicio de Usuario
 * Define los casos de uso del dominio
 */
public interface IUsuarioServicePort {

    Usuario guardarUsuario(Usuario usuario);

}
