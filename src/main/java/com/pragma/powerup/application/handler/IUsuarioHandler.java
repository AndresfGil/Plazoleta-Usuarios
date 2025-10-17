package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.UsuarioRequestDto;
import com.pragma.powerup.application.dto.response.UsuarioResponseDto;

public interface IUsuarioHandler {

    UsuarioResponseDto guardarUsuario(UsuarioRequestDto usuarioRequestDto);
    
    UsuarioResponseDto obtenerUsuarioPorId(Long id);
}

