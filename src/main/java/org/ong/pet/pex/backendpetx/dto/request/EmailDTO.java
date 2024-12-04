package org.ong.pet.pex.backendpetx.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(

        @JsonProperty(value = "email")
        @Email(message = "Email inválido")
        @NotBlank(message = "campo email é obrigatório")
        String email
) {
}
