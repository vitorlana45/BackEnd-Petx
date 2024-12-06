package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.validation.annotation.Validated;

@Validated
public record AtualizarTutorRequisicao(

        @NotBlank(message = "O campo cpf é obrigatório")
        @CPF(message = "O campo cpf é inválido")
        String cpf,
        String nome,
        @NotBlank(message = "O campo cep é obrigatório")
        String cep,
        Integer idade,
        String telefone,
        String cidade,
        String bairro,
        String rua
) {
}
