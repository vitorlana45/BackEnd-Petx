package org.ong.pet.pex.backendpetx.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TutorResposta(

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
        LocalDateTime criadoEm,
        Set<AnimalGenericoResposta> listaDeAnimais
) {
}
