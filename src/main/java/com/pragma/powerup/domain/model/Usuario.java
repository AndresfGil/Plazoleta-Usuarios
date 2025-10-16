package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer documentoIdentidad;
    private String celular;
    private Date fechaNacimiento;
    private String correo;
    private String clave;
    private Long idRol;
    private Boolean activo;
}
