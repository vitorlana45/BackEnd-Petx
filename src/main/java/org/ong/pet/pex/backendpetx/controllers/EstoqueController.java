package org.ong.pet.pex.backendpetx.controllers;

import org.ong.pet.pex.backendpetx.dto.response.EstoqueResponseDTO;
import org.ong.pet.pex.backendpetx.dto.response.ListarEstoqueResponse;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }


    @GetMapping("buscar/{id}")
    public ResponseEntity<EstoqueResponseDTO> pegarEstoquePorId(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(estoqueService.pegarEstoquePorId(id));
    }

    @GetMapping("/quantidade")
    public ResponseEntity<RacaoDisponivelResposta> calcularQuantidadeDeRacaoDias() {
        return ResponseEntity.ok().body(estoqueService.calcularQuantidadeRacao());
    }

    @GetMapping()
    public ResponseEntity<ListarEstoqueResponse> listarEstoque() {
        return ResponseEntity.ok().body(estoqueService.listarEstoque());
    }

    @GetMapping("/produto")
    public ResponseEntity<ListarEstoqueResponse> listarEstoquePorTipoProduto(@RequestParam(value = "produto") String produto) {
        return ResponseEntity.ok().body(estoqueService.listarEstoquePorTipoProduto(produto));
    }

}
