package org.ong.pet.pex.backendpetx.controllers;

import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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


    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/racao")
    public ResponseEntity<RacaoDisponivelResposta> calcularQuantidadeDeRacaoDias() {
        return ResponseEntity.ok().body(estoqueService.calcularQuantidadeRacao());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping()
    public ResponseEntity<Page<ProdutoDTOResposta>> paginarProdutos(
            @RequestParam(value = "tipo", required = false) TipoProduto tipoProduto,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "quantidade", required = false) Double quantidade,
            @RequestParam(value = "medida", required = false) UnidadeDeMedidaEnum medida,
            @RequestParam(value = "chave", required = false) String chave,
            Pageable pageable) {

        return ResponseEntity.ok(estoqueService.paginarProdutoEstoque(tipoProduto, nome, quantidade, medida, chave,pageable));
    }

}
