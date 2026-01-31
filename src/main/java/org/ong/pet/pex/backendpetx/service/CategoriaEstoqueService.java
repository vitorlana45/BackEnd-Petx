package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.controllers.estoque.EstoqueDTO;
import org.springframework.data.domain.Page;
import org.ong.pet.pex.backendpetx.dto.categoria_estoque.*;

import java.util.List;

public interface CategoriaEstoqueService {

    CreateCategoriaEstoqueResponse createCategoriaEstoque(CreateCategoriaEstoqueRequest request);
    GetCategoriaEstoqueResponse getCategoriaEstoque(GetCategoriaEstoqueRequest request);
    UpdateCategoriaEstoqueResponse updateCategoriaEstoque(UpdateCategoriaEstoqueRequest request);
    DeleteCategoriaEstoqueResponse deleteCategoriaEstoque(DeleteCategoriaEstoqueRequest request);
    Page<ListCategoriaEstoqueResponse> listCategoriaEstoque(ListCategoriaEstoqueRequest request);

    List<EstoqueDTO> listAllEstoques();
}
