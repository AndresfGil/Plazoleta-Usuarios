package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUsuarioServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Usuario;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUsuarioPersistencePort;

import java.util.Calendar;
import java.util.Date;

public class UsuarioUseCase implements IUsuarioServicePort {

    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public UsuarioUseCase(IUsuarioPersistencePort usuarioPersistencePort, 
                          IPasswordEncoderPort passwordEncoderPort) {
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        // Validar que el usuario sea mayor de edad
        validarMayorDeEdad(usuario.getFechaNacimiento());

        // Encriptar la contraseña
        String claveEncriptada = passwordEncoderPort.encode(usuario.getClave());
        usuario.setClave(claveEncriptada);

        // Establecer el usuario como activo
        usuario.setActivo(true);
        
        // Guardar el usuario y retornarlo
        return usuarioPersistencePort.guardarUsuario(usuario);
    }


    private void validarMayorDeEdad(Date fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new DomainException("La fecha de nacimiento es obligatoria");
        }

        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(fechaNacimiento);

        Calendar fechaActual = Calendar.getInstance();

        int edad = fechaActual.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);

        if (fechaActual.get(Calendar.DAY_OF_YEAR) < fechaNac.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }

        if (edad < 18) {
            throw new DomainException("El usuario debe ser mayor de 18 años");
        }
    }
}
