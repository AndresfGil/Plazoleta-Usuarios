package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.dto.response.LoginResponseDto;
import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Usuario;
import com.pragma.powerup.domain.model.Rol;
import com.pragma.powerup.domain.spi.IUsuarioPersistencePort;
import com.pragma.powerup.domain.spi.IRolPersistencePort;
import com.pragma.powerup.infrastructure.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {

    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final IRolPersistencePort rolPersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Usuario usuario = usuarioPersistencePort.obtenerUsuarioPorCorreo(loginRequestDto.getCorreo());
        
        if (usuario == null) {
            throw new DomainException("Credenciales inválidas");
        }

        if (!passwordEncoder.matches(loginRequestDto.getContrasena(), usuario.getClave())) {
            throw new DomainException("Credenciales inválidas");
        }

        if (!usuario.getActivo()) {
            throw new DomainException("Usuario inactivo");
        }

        Rol rol = rolPersistencePort.obtenerRolPorId(usuario.getIdRol());
        
        String token = jwtUtils.generateToken(usuario.getCorreo(), rol.getNombreRol(), usuario.getId());

        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        response.setCorreo(usuario.getCorreo());
        response.setRol(rol.getNombreRol());

        return response;
    }
}
