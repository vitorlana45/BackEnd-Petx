package org.ong.pet.pex.backendpetx.service.validation;


import jakarta.validation.*;

import java.lang.annotation.*;

@Constraint(validatedBy = ValidadorDeInsercaoDeUsuario.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface InserirUsuarioValido {
	String message() default "Validation error";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
