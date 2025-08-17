package org.ong.pet.pex.backendpetx.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.enums.MaturidadeEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;

// src/main/java/.../validation/MaeFilhotesValidator.java
public class MaeFilhotesValidator implements ConstraintValidator<MaeFilhotesConsistente, AnimalDTO> {

    @Override
    public boolean isValid(AnimalDTO a, ConstraintValidatorContext ctx) {
        if (a == null) return true;
        boolean ok = true;
        ctx.disableDefaultConstraintViolation();

        boolean maezinha = a.isAnimalEMaezinha();

        if (maezinha) {
            // Regra 1: Só FEMEA pode ser mãezinha
            if (a.getSexo() != SexoEnum.FEMEA) {
                fieldError(ctx, "Apenas fêmeas podem ser marcadas como mãezinhas.", "sexo");
                ok = false;
            }
            // Regra 2: Maturidade não pode ser FILHOTE
            if (a.getMaturidade() == MaturidadeEnum.FILHOTE) {
                fieldError(ctx, "Filhote não pode ser 'mãezinha com filhotes'.", "maturidade");
                ok = false;
            }
            // Regra 3: Quantidades obrigatórias
            var mf = a.getMaezinhaComFilhotes();
            if (mf == null) {
                fieldError(ctx, "Informe as quantidades de machos/fêmeas.", "maezinhaComFilhotes.quantidadeMacho");
                fieldError(ctx, "Informe as quantidades de machos/fêmeas.", "maezinhaComFilhotes.quantidadeFemea");
                ok = false;
            } else {
                if (mf.getQuantidadeMacho() == null) {
                    fieldError(ctx, "Informe a quantidade de machos.", "maezinhaComFilhotes.quantidadeMacho");
                    ok = false;
                }
                if (mf.getQuantidadeFemea() == null) {
                    fieldError(ctx, "Informe a quantidade de fêmeas.", "maezinhaComFilhotes.quantidadeFemea");
                    ok = false;
                }
            }
        } else {
            // Opcional: se não é mãezinha, zere/ignorar quantidades > 0
            var mf = a.getMaezinhaComFilhotes();
            if (mf != null && ((mf.getQuantidadeMacho() != null && mf.getQuantidadeMacho() > 0)
                    || (mf.getQuantidadeFemea() != null && mf.getQuantidadeFemea() > 0))) {
                globalError(ctx, "Desmarque 'mãezinha' ou informe 0 nas quantidades.");
                ok = false;
            }
        }
        return ok;
    }

    private void fieldError(ConstraintValidatorContext ctx, String msg, String property) {
        ctx.buildConstraintViolationWithTemplate(msg)
           .addPropertyNode(property)
           .addConstraintViolation();
    }
    private void globalError(ConstraintValidatorContext ctx, String msg) {
        ctx.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
