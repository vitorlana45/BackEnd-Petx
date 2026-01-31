package org.ong.pet.pex.backendpetx.service.impl;

import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.controllers.estoque.EstoqueDTO;
import org.ong.pet.pex.backendpetx.dto.categoria_estoque.*;
import org.ong.pet.pex.backendpetx.entities.CategoriaEstoque;
import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.ong.pet.pex.backendpetx.repositories.CategoriaEstoqueRepository;
import org.ong.pet.pex.backendpetx.repositories.EstoqueRepository;
import org.ong.pet.pex.backendpetx.service.CategoriaEstoqueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaEstoqueServiceImpl implements CategoriaEstoqueService {

    private final CategoriaEstoqueRepository categoriaEstoqueRepository;
    private final EstoqueRepository estoqueRepository;

    @Override
    public CreateCategoriaEstoqueResponse createCategoriaEstoque(CreateCategoriaEstoqueRequest request) {
        // Verificar se o estoque existe
        Estoque estoque = estoqueRepository.findById(request.getEstoqueId())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        // Verificar se já existe categoria com o mesmo nome no estoque
        if (categoriaEstoqueRepository.findByNomeAndEstoqueId(request.getNome(), request.getEstoqueId()).isPresent()) {
            throw new RuntimeException("Já existe uma categoria com este nome neste estoque");
        }

        CategoriaEstoque categoria = CategoriaEstoque.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .estoque(estoque)
                .build();

        categoria = categoriaEstoqueRepository.save(categoria);

        return new CreateCategoriaEstoqueResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                estoque.getId(),
                estoque.getNome(),
                categoria.getCriadoEm(),
                "Categoria criada com sucesso"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GetCategoriaEstoqueResponse getCategoriaEstoque(GetCategoriaEstoqueRequest request) {
        CategoriaEstoque categoria = categoriaEstoqueRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        return new GetCategoriaEstoqueResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getEstoque().getId(),
                categoria.getEstoque().getNome(),
                categoria.getCriadoEm(),
                categoria.getAtualizadoEm()
        );
    }

    @Override
    public UpdateCategoriaEstoqueResponse updateCategoriaEstoque(UpdateCategoriaEstoqueRequest request) {
        CategoriaEstoque categoria = categoriaEstoqueRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Estoque estoque = estoqueRepository.findById(request.getEstoqueId())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        // Verificar se já existe categoria com o mesmo nome no estoque (exceto a atual)
        if (categoriaEstoqueRepository.existsByNomeAndEstoqueIdAndIdNot(request.getNome(), request.getEstoqueId(), request.getId())) {
            throw new RuntimeException("Já existe uma categoria com este nome neste estoque");
        }

        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());
        categoria.setEstoque(estoque);

        categoria = categoriaEstoqueRepository.save(categoria);

        return new UpdateCategoriaEstoqueResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                estoque.getId(),
                estoque.getNome(),
                categoria.getAtualizadoEm(),
                "Categoria atualizada com sucesso"
        );
    }

    @Override
    public DeleteCategoriaEstoqueResponse deleteCategoriaEstoque(DeleteCategoriaEstoqueRequest request) {
        CategoriaEstoque categoria = categoriaEstoqueRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        String nome = categoria.getNome();
        categoriaEstoqueRepository.delete(categoria);

        return new DeleteCategoriaEstoqueResponse(
                request.getId(),
                nome,
                "Categoria excluída com sucesso",
                true
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListCategoriaEstoqueResponse> listCategoriaEstoque(ListCategoriaEstoqueRequest request) {
        Sort.Direction direction = request.getDirection().equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(direction, request.getSort())
        );

        Page<CategoriaEstoque> categorias = categoriaEstoqueRepository.findByFilters(
                request.getNome(),
                request.getEstoqueId(),
                pageable
        );

        return categorias.map(categoria -> new ListCategoriaEstoqueResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getEstoque().getId(),
                categoria.getEstoque().getNome(),
                categoria.getCriadoEm(),
                categoria.getAtualizadoEm()
        ));
    }

    @Override
    public List<EstoqueDTO> listAllEstoques() {
       return estoqueRepository.findAll().stream().map(estoque -> {
              return new EstoqueDTO(estoque.getId(), estoque.getNome(), estoque.getDescricao(), "","");
       }).toList();

    }
}
