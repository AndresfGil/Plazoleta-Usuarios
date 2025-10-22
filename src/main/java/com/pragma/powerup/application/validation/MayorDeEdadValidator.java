package com.pragma.powerup.application.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

public class MayorDeEdadValidator implements ConstraintValidator<MayorDeEdad, Date> {

    @Override
    public void initialize(MayorDeEdad constraintAnnotation) {
        // No se necesita inicialización
    }

    @Override
    public boolean isValid(Date fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return true; // Dejamos que @NotNull maneje este caso
        }

        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(fechaNacimiento);

        Calendar fechaActual = Calendar.getInstance();

        int edad = fechaActual.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);

        // Ajustar si aún no ha cumplido años este año
        if (fechaActual.get(Calendar.DAY_OF_YEAR) < fechaNac.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }

        return edad >= 18;
    }
}
