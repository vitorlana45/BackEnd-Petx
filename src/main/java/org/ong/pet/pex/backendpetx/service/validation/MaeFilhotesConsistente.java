package org.ong.pet.pex.backendpetx.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// src/main/java/.../validation/MaeFilhotesConsistente.java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaeFilhotesValidator.class)
public @interface MaeFilhotesConsistente {
    String message() default "Dados inconsistentes para 'mãezinha com filhotes'.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
