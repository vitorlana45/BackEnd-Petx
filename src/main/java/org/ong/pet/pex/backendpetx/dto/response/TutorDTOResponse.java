package org.ong.pet.pex.backendpetx.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;

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
        Set<AnimalGenericoResposta> listaDeAnimais
) {
}
