package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotNull;

public record CadastrarTutorRequisicao(

        @NotNull(message = "O campo cpf é obrigatório")
        String cpf,
        String Nome,
        String idade,
        String cidade,
        String bairro,
        String rua,
        String chipAnimal
) {
}
