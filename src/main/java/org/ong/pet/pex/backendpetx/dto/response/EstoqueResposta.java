package org.ong.pet.pex.backendpetx.dto.response;

import java.util.List;

public record EstoqueResposta(
        Long id,
        String criadoEm,
        String atualizadoEm,
        List<ProdutoResposta> produtos
        ) {
}
