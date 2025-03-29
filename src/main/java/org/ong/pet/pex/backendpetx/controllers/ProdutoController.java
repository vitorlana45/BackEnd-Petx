package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> criarProduto(@RequestBody @Valid ProdutoDTO produto) {
        Long id = produtoService.cadastrarProduto(produto);
        URI location = URI.create("/api/produtos/" + id);
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTOResposta> buscarProdutoPorId(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(produtoService.buscarProdutoPorId(id));
    }

  //TODO: Implementar validação de campos dinâmicos, ainda exige mais informações
    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoDTOResposta> atualizarProduto(@PathVariable(value = "id") Long id, @RequestBody  ProdutoDTO produtoAtualizado) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, produtoAtualizado));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable(value = "id") Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}



























