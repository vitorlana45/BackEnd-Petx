package org.ong.pet.pex.backendpetx.dto.response;

import org.ong.pet.pex.backendpetx.entities.UserRole;

public record RespostaCricaoUsuario(Long id, String email, UserRole role) {
}
