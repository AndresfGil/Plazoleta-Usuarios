package com.pragma.powerup.application.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MayorDeEdadValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MayorDeEdad {
    
    String message() default "El usuario debe ser mayor de 18 años";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
