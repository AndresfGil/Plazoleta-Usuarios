package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.UsuarioRequestDto;
import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.application.handler.IUsuarioHandler;
import com.pragma.powerup.application.mapper.IUsuarioRequestMapper;
import com.pragma.powerup.application.mapper.IUsuarioResponseMapper;
import com.pragma.powerup.domain.api.IUsuarioServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsuarioHandler implements IUsuarioHandler {

    private final IUsuarioServicePort usuarioServicePort;
    private final IUsuarioRequestMapper usuarioRequestMapper;
    private final IUsuarioResponseMapper usuarioResponseMapper;

    @Override
    public UsuarioResponseDto guardarUsuario(UsuarioRequestDto usuarioRequestDto) {
        validarAutorizacionParaCrearUsuario(usuarioRequestDto.getIdRol());
        
        Usuario usuario = usuarioRequestMapper.toUsuario(usuarioRequestDto);
        Usuario usuarioGuardado = usuarioServicePort.guardarUsuario(usuario);
        return usuarioResponseMapper.toResponse(usuarioGuardado);
    }

    private void validarAutorizacionParaCrearUsuario(Long idRolUsuario) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new DomainException("Usuario no autenticado");
        }

        String rolUsuarioAutenticado = authentication.getAuthorities().iterator().next().getAuthority();
        log.info("Usuario autenticado con rol: {} intentando crear usuario con rol: {}", 
                rolUsuarioAutenticado, idRolUsuario);

        if (idRolUsuario == 2L) {
            if (!"ADMINISTRADOR".equals(rolUsuarioAutenticado)) {
                throw new DomainException("Solo los administradores pueden crear usuarios propietarios");
            }
        } else if (idRolUsuario == 3L) {
            if (!"PROPIETARIO".equals(rolUsuarioAutenticado)) {
                throw new DomainException("Solo los propietarios pueden crear usuarios empleados");
            }
        } else if (idRolUsuario == 1L) {
            if (!"ADMINISTRADOR".equals(rolUsuarioAutenticado)) {
                throw new DomainException("Solo los administradores pueden crear otros administradores");
            }
        } else {
            throw new DomainException("Rol de usuario no válido");
        }
        
        log.info("Autorización validada correctamente para crear usuario con rol: {}", idRolUsuario);
    }

    @Override
    public UsuarioResponseDto obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioServicePort.obtenerUsuarioPorId(id);
        return usuarioResponseMapper.toResponse(usuario);
    }
}

