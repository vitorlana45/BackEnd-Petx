package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDTO;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDinamicaDTO;
import org.ong.pet.pex.backendpetx.dto.response.DespesaDTORespota;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.service.DespesaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/despesa")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @PostMapping
    ResponseEntity<DespesaDTORespota> cadastrarDespesa(@RequestBody @Valid final DespesaRequisicaoDTO dto) {
        return ResponseEntity.ok().body(despesaService.cadastrarDespesa(dto));
    }

    @GetMapping("/listar")
    ResponseEntity<List<ListarDespesaResposta>> listarDespesa() {
        return ResponseEntity.ok().body(despesaService.listarDespesa());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarDespesa(@PathVariable(value = "id") final Long id) {
        despesaService.deletarDespesa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtrar/{filtro}")
    ResponseEntity<List<ListarDespesaResposta>> listarDespesaFiltrada(@PathVariable(value = "filtro") final String filtro) {
        return ResponseEntity.ok().body(despesaService.listarDespesaFiltrada(filtro));
    }

    @PatchMapping("/atualizar/{id}")
    ResponseEntity<DespesaDTORespota> atualizarDespesaDinamicamente(@PathVariable(value = "id") final Long id,
                                                       @RequestBody final DespesaRequisicaoDinamicaDTO dto) {
        return ResponseEntity.ok().body(despesaService.atualizarDespesaDinamicamente(id, dto));
    }

    @PostMapping("/{id}")
    ResponseEntity<DespesaDTORespota> atualizarDespesa(@PathVariable(value = "id") final Long id, @RequestBody @Valid final DespesaRequisicaoDTO dto) {
        return ResponseEntity.ok().body(despesaService.atualizarDespesa(id, dto));
    }

}