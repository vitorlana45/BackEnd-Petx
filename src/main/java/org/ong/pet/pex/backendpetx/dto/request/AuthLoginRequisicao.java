package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

@Validated
public record AuthLoginRequisicao(

        @Email(message = "deve ser um endereço de e-mail válido, verifique os dados novamente!")
        String email,
        @NotBlank(message = "não pode ser nulo ou vazio, verifique os dados novamente!")
        @Size(min = 6, message = "deve ter no mínimo 6 caracteres, verifique os dados novamente!")
        String password){
}
