package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.entities.Produto;
import org.ong.pet.pex.backendpetx.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> criarProduto(@RequestBody ProdutoDTO produto) {
        Long id = produtoService.cadastrarProduto(produto);
        URI location = URI.create("/api/produtos/" + id);
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<ProdutoDTOResposta>> listarProdutos() {
        return ResponseEntity.ok(produtoService.listarProdutos());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(produtoService.buscarProdutoPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoDTOResposta> atualizarProduto(@PathVariable(value = "id") Long id, @RequestBody @Valid ProdutoDTO produtoAtualizado) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, produtoAtualizado));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable(value = "id") Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}



























