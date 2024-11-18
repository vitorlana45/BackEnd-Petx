package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarEstoqueRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public RacaoDisponivelResposta calcularQuantidadeDeRacaoDias() {
        return estoqueService.calcularQuantidadeRacao();
    }


}
