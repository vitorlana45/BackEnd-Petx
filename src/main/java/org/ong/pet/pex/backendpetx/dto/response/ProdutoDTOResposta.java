package org.ong.pet.pex.backendpetx.dto.response;


import lombok.Builder;
import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;

@Builder
public record ProdutoDTOResposta (
        Long id,
        ProdutoDTO produtoDTO
) {

}
