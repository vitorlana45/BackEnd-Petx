package org.ong.pet.pex.backendpetx.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ong.pet.pex.backendpetx.controllers.exceptions.FieldMessage;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EnumValidator.class);

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> valor, ConstraintValidatorContext context) {
        logger.info("Validando valor do enum: {}", valor);

        List<FieldMessage> list = new ArrayList<>();

        // Valida se o valor é nulo
        if (valor == null) {
            var v = Arrays.asList(enumClass.getEnumConstants());
            logger.info("Valor do enum é nulo.");
            list.add(new FieldMessage(null, "O valor não pode ser nulo."));
        } else {
            // Verifica se o valor existe no enum
            logger.info("Verificando se o valor {} existe no enum.", valor);
            boolean valido = Arrays.asList(enumClass.getEnumConstants()).contains(valor);

            if (!valido) {
                list.add(new FieldMessage(null, "Valor inválido: " + valor + ". Valores esperados: " + Arrays.toString(enumClass.getEnumConstants())));
            }
        }

        // Adiciona as violações ao contexto
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}