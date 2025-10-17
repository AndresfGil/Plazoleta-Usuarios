package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Información de un usuario")
public class UsuarioResponseDto {

    
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;
    
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Correo electrónico", example = "juan.perez@example.com")
    private String correo;
    
    @Schema(description = "Estado del usuario (true=activo, false=inactivo)", example = "true")
    private Boolean activo;
}
