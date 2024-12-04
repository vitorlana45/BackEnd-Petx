package org.ong.pet.pex.backendpetx.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.ong.pet.pex.backendpetx.entities.Usuario;
import org.ong.pet.pex.backendpetx.service.validation.InserirUsuarioValido;

@JsonIgnoreProperties(ignoreUnknown = true)
@InserirUsuarioValido
public record UsuarioDTO(

        @Email(message = "O campo email deve ser um email válido")
        @NotBlank(message = "Email é obrigatório")
        @Length(min = 3, max = 320, message = "Email deve ter entre 3 e 320 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Length(min = 6, max = 30, message = "Senha deve ter entre 6 e 50 caracteres")
        String password
) {
    public UsuarioDTO(Usuario usuario) {
        this(usuario.getEmail(), usuario.getPassword());
    }
}
