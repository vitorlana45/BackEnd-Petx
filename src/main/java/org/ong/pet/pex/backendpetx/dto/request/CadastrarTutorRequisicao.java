package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record CadastrarTutorRequisicao(

        @NotBlank(message = "O campo cpf é obrigatório")
        String cpf,
        String nome,
        @NotBlank(message = "O campo cep é obrigatório")
        String cep,
        Integer idade,
        String telefone,
        String cidade,
        String bairro,
        String rua,
        @NotBlank(message = "É preciso informar o Chip do animal")
        String chipAnimal

) {
}
