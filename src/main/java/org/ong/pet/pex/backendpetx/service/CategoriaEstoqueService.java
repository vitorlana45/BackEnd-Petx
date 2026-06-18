package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.EstoqueDTO;
import org.springframework.data.domain.Page;
import org.ong.pet.pex.backendpetx.dto.categoria_estoque.*;

import java.util.List;

public interface CategoriaEstoqueService {

    CriarCategoriaEstoqueResposta createCategoriaEstoque(CriarCategoriaEstoqueRequisicao request);
    BuscarCategoriaEstoqueResposta getCategoriaEstoque(BuscarCategoriaEstoqueRequisicao request);
    AtualizarCategoriaEstoqueResposta updateCategoriaEstoque(AtualizarCategoriaEstoqueRequisicao request);
    ExcluirCategoriaEstoqueResposta deleteCategoriaEstoque(ExcluirCategoriaEstoqueRequisicao request);
    Page<ListarCategoriaEstoqueResposta> listCategoriaEstoque(ListarCategoriaEstoqueRequisicao request);

    List<EstoqueDTO> listAllEstoques();
}
