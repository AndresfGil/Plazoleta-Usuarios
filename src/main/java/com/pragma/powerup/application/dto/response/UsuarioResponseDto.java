package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Schema(description = "Información de un usuario")
public class UsuarioResponseDto {
    
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;
    
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;
    
    @Schema(description = "Número de documento de identidad", example = "12345678")
    private Integer documentoIdentidad;
    
    @Schema(description = "Número de celular", example = "+573001234567")
    private String celular;
    
    @Schema(description = "Fecha de nacimiento", example = "1995-01-15")
    private Date fechaNacimiento;
    
    @Schema(description = "Correo electrónico", example = "juan.perez@example.com")
    private String correo;
    
    @Schema(description = "ID del rol (1=ADMINISTRADOR, 2=PROPIETARIO, 3=EMPLEADO, 4=CLIENTE)", example = "4")
    private Long idRol;
    
    @Schema(description = "Estado del usuario (true=activo, false=inactivo)", example = "true")
    private Boolean activo;
}
