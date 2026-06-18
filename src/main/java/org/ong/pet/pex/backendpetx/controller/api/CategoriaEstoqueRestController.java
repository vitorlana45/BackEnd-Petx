package org.ong.pet.pex.backendpetx.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.categoria_estoque.*;
import org.ong.pet.pex.backendpetx.service.CategoriaEstoqueService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias-estoque")
@RequiredArgsConstructor
@Tag(name = "Categoria Estoque", description = "Operações relacionadas às categorias de estoque")
public class CategoriaEstoqueRestController {

    private final CategoriaEstoqueService categoriaEstoqueService;

    @PostMapping
    @Operation(summary = "Criar nova categoria de estoque")
    public ResponseEntity<CriarCategoriaEstoqueResposta> createCategoriaEstoque(
            @Valid @RequestBody CriarCategoriaEstoqueRequisicao request) {
        try {
            CriarCategoriaEstoqueResposta response = categoriaEstoqueService.createCategoriaEstoque(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar categoria de estoque: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria de estoque por ID")
    public ResponseEntity<BuscarCategoriaEstoqueResposta> getCategoriaEstoque(@PathVariable Long id) {
        try {
            BuscarCategoriaEstoqueRequisicao request = new BuscarCategoriaEstoqueRequisicao(id);
            BuscarCategoriaEstoqueResposta response = categoriaEstoqueService.getCategoriaEstoque(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria de estoque")
    public ResponseEntity<AtualizarCategoriaEstoqueResposta> updateCategoriaEstoque(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarCategoriaEstoqueRequisicao request) {
        try {
            request.setId(id);
            AtualizarCategoriaEstoqueResposta response = categoriaEstoqueService.updateCategoriaEstoque(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar categoria de estoque: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria de estoque")
    public ResponseEntity<ExcluirCategoriaEstoqueResposta> deleteCategoriaEstoque(@PathVariable Long id) {
        try {
            ExcluirCategoriaEstoqueRequisicao request = new ExcluirCategoriaEstoqueRequisicao(id);
            ExcluirCategoriaEstoqueResposta response = categoriaEstoqueService.deleteCategoriaEstoque(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar categorias de estoque com filtros e paginação")
    public ResponseEntity<Page<ListarCategoriaEstoqueResposta>> listCategoriaEstoque(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long estoqueId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        ListarCategoriaEstoqueRequisicao request = new ListarCategoriaEstoqueRequisicao(
                nome, estoqueId, page, size, sort, direction);

        Page<ListarCategoriaEstoqueResposta> response = categoriaEstoqueService.listCategoriaEstoque(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estoque/{estoqueId}")
    @Operation(summary = "Listar categorias por estoque específico")
    public ResponseEntity<Page<ListarCategoriaEstoqueResposta>> listCategoriasByEstoque(
            @PathVariable Long estoqueId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        ListarCategoriaEstoqueRequisicao request = new ListarCategoriaEstoqueRequisicao(
                null, estoqueId, page, size, sort, direction);

        Page<ListarCategoriaEstoqueResposta> response = categoriaEstoqueService.listCategoriaEstoque(request);
        return ResponseEntity.ok(response);
    }
}
