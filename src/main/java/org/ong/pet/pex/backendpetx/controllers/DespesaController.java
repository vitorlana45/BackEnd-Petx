package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDTO;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDinamicaDTO;
import org.ong.pet.pex.backendpetx.dto.response.DespesaDTORespota;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;
import org.ong.pet.pex.backendpetx.service.DespesaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/despesa")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping
    ResponseEntity<DespesaDTORespota> cadastrarDespesa(@RequestBody @Valid final DespesaRequisicaoDTO dto) {
        return ResponseEntity.ok().body(despesaService.cadastrarDespesa(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarDespesa(@PathVariable(value = "id") final Long id) {
        despesaService.deletarDespesa(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping()
    ResponseEntity<Page<ListarDespesaResposta>> paginarDespesa(
            @RequestParam(value = "descricao", required = false) final String descricao,
            @RequestParam(value = "categoria", required = false) final CategoriaDespesaEnum categoria,
            @RequestParam(value = "status", required = false) final StatusDespesaEnum status,
            @RequestParam(value = "formaPagamento", required = false) final FormaPagamentoEnum formaPagamento,
            @RequestParam(value = "dataPagamento", required = false) final LocalDate dataPagamento,
            @RequestParam(value = "pagamentoPrevisto", required = false) final LocalDate dataPrevistaPagamento,
            @RequestParam(value = "valor", required = false) final BigDecimal valor,
            Pageable pageable
    ) {
        Page<ListarDespesaResposta> result = despesaService.paginarDespesa(
                descricao,
                categoria,
                status,
                formaPagamento,
                dataPagamento,
                dataPrevistaPagamento,
                valor,
                pageable
        );
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PatchMapping("/{id}")
    ResponseEntity<DespesaDTORespota> atualizarDespesaDinamicamente(@PathVariable(value = "id") final Long id,
                                                       @RequestBody final DespesaRequisicaoDinamicaDTO dto) {
        return ResponseEntity.ok().body(despesaService.atualizarDespesaDinamicamente(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping("/{id}")
    ResponseEntity<DespesaDTORespota> atualizarDespesa(@PathVariable(value = "id") final Long id, @RequestBody @Valid final DespesaRequisicaoDTO dto) {
        return ResponseEntity.ok().body(despesaService.atualizarDespesa(id, dto));
    }

}