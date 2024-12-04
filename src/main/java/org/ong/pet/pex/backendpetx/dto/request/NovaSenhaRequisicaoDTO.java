package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record NovaSenhaRequisicaoDTO (

        @NotBlank(message = "campo senha é obrigatório")
        String token,

        @NotBlank(message = "campo senha é obrigatório")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password
) {
}
