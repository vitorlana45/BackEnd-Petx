package org.ong.pet.pex.backendpetx.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Set;

@Builder
public record TutorDTOResponse(
        String cpf,
        String nome,
        @NotBlank(message = "O campo cpf é obrigatório")
        String cep,
        int idade,
        String telefone,
        String cidade,
        String bairro,
        String rua,
        Set<String> chipAnimal
) {
}
