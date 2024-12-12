package org.ong.pet.pex.backendpetx.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Set;

@Builder
public record TutorDTOResposta(

        Long id,
        String cpf,
        String nome,
        @NotBlank(message = "O campo cpf é obrigatório")
        String cep,
        int idade,
        String estado,
        String telefone,
        String cidade,
        String bairro,
        String rua,
        Set<AnimalGenericoResposta> listaDeAnimais
) {
}
