package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.ong.pet.pex.backendpetx.entities.Usuario;
import org.ong.pet.pex.backendpetx.service.validation.InserirUsuarioValido;

@InserirUsuarioValido
public record UsuarioDTO (

        @NotBlank(message = "Email é obrigatório")
        @Length(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Length(min = 6, max = 30, message = "Senha deve ter entre 6 e 50 caracteres")
        String password
){
    public UsuarioDTO (Usuario usuario){
        this(usuario.getEmail(), usuario.getPassword());
    }
}
