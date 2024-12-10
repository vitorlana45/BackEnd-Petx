package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.ConsumoAlimentoRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.ConsumoAlimentoResposta;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;

import java.util.List;

public interface ConsumoAlimentoService {

    ConsumoAlimentoResposta cadastrarConsumoAlimento(ConsumoAlimentoRequisicao dto);

    ConsumoAlimentoResposta buscarPorPorte(PorteEnum porte);

    List<ConsumoAlimentoResposta> buscarTodos();

    ConsumoAlimentoResposta atualizar( ConsumoAlimentoRequisicao consumoAlimento);

    void deletarPorPorte(PorteEnum porte);


}
