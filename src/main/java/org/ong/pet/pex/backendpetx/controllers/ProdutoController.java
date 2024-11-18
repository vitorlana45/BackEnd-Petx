package org.ong.pet.pex.backendpetx.controllers;

import org.ong.pet.pex.backendpetx.entities.Produto;
import org.ong.pet.pex.backendpetx.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    public Produto cadastrarProduto(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Produto buscarProdutoPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @PutMapping("/{id}")
    public Produto atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        Produto produtoExistente = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
        
        return produtoRepository.save(produtoExistente);
    }

    @DeleteMapping("/{id}")
    public void excluirProduto(@PathVariable Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        produtoRepository.delete(produto);
    }
}