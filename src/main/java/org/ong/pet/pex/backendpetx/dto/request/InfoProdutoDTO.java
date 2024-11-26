package org.ong.pet.pex.backendpetx.dto.request;

import lombok.Builder;

@Builder
public record InfoProdutoDTO(
        String type,
        String value
) {
}
