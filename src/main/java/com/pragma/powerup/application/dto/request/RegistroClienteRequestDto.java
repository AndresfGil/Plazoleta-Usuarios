package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@Schema(description = "Datos necesarios para crear un nuevo usuario")
public class RegistroClienteRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del usuario", example = "Juan", required = true)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del usuario", example = "Pérez", required = true)
    private String apellido;

    @NotNull(message = "El documento de identidad es obligatorio")
    @Schema(description = "Número de documento de identidad", example = "12345678", required = true)
    private Integer documentoIdentidad;

    @NotBlank(message = "El celular es obligatorio")
    @Pattern(regexp = "^\\+?\\d{10,13}$", message = "El celular debe tener entre 10 y 13 dígitos")
    @Schema(description = "Número de celular con código de país opcional", example = "+573001234567", required = true)
    private String celular;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com", required = true)
    private String correo;

    @NotBlank(message = "La clave es obligatoria")
    private String clave;
}
