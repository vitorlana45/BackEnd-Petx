package org.ong.pet.pex.backendpetx.dto.response;

import lombok.*;

import java.util.Date;

public record AuthLoginResposta(String token, Long expiration) {
}
