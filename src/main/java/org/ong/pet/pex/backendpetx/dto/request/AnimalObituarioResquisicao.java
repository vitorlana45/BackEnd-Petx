package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Validated
public record AnimalObituarioResquisicao(

        @NotNull(message = "O id do animal não pode ser nulo")
        String chipId,
        @NotNull(message = "O motivo do óbito não pode ser nulo")
        String motivoObito,
        @NotNull(message = "A data do óbito não pode ser nula")
        Date dataObito
){
}
