package org.ong.pet.pex.backendpetx.controllers;

import org.ong.pet.pex.backendpetx.dto.response.ListarEstoqueResponse;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping
    public void cadastrarDespesa(@RequestBody @Valid CadastrarEstoqueRequisicao cadastrarEstoqueRequisicao) {
        estoqueService.salvarEstoque(cadastrarEstoqueRequisicao);
    }

    @GetMapping()
    public ResponseEntity<ListarEstoqueResponse> listarEstoque() {
        return ResponseEntity.ok().body(estoqueService.listarEstoque());
    }

}
