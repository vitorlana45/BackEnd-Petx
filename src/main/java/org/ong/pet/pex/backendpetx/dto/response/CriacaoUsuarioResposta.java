package org.ong.pet.pex.backendpetx.dto.response;

import org.ong.pet.pex.backendpetx.entity.UserRole;

public record CriacaoUsuarioResposta(Long id, String nome, String email, UserRole role) {
}
