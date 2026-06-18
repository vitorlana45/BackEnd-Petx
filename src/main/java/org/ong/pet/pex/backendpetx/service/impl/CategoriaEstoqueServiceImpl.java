package org.ong.pet.pex.backendpetx.service.impl;

import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.EstoqueDTO;
import org.ong.pet.pex.backendpetx.dto.categoria_estoque.*;
import org.ong.pet.pex.backendpetx.entity.CategoriaEstoque;
import org.ong.pet.pex.backendpetx.entity.Estoque;
import org.ong.pet.pex.backendpetx.repository.CategoriaEstoqueRepository;
import org.ong.pet.pex.backendpetx.repository.EstoqueRepository;
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
    public CriarCategoriaEstoqueResposta createCategoriaEstoque(CriarCategoriaEstoqueRequisicao request) {
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

        return new CriarCategoriaEstoqueResposta(
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
    public BuscarCategoriaEstoqueResposta getCategoriaEstoque(BuscarCategoriaEstoqueRequisicao request) {
        CategoriaEstoque categoria = categoriaEstoqueRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        return new BuscarCategoriaEstoqueResposta(
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
    public AtualizarCategoriaEstoqueResposta updateCategoriaEstoque(AtualizarCategoriaEstoqueRequisicao request) {
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

        return new AtualizarCategoriaEstoqueResposta(
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
    public ExcluirCategoriaEstoqueResposta deleteCategoriaEstoque(ExcluirCategoriaEstoqueRequisicao request) {
        CategoriaEstoque categoria = categoriaEstoqueRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        String nome = categoria.getNome();
        categoriaEstoqueRepository.delete(categoria);

        return new ExcluirCategoriaEstoqueResposta(
                request.getId(),
                nome,
                "Categoria excluída com sucesso",
                true
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListarCategoriaEstoqueResposta> listCategoriaEstoque(ListarCategoriaEstoqueRequisicao request) {
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

        return categorias.map(categoria -> new ListarCategoriaEstoqueResposta(
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
