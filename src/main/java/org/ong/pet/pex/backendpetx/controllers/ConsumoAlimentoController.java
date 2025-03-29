package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.ConsumoAlimentoRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.ConsumoAlimentoResposta;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.service.ConsumoAlimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/consumo")
public class ConsumoAlimentoController {

    private final ConsumoAlimentoService consumoAlimentoService;

    public ConsumoAlimentoController(ConsumoAlimentoService consumoAlimentoService) {
        this.consumoAlimentoService = consumoAlimentoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping
    public ResponseEntity<ConsumoAlimentoResposta> cadastrarConsumo(@RequestBody @Valid final ConsumoAlimentoRequisicao dto) {
        ConsumoAlimentoResposta entidade = consumoAlimentoService.cadastrarConsumoAlimento(dto);
        URI uri = URI.create("/api/consumo/" + entidade.id());
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/{porte}")
    public ResponseEntity<ConsumoAlimentoResposta> buscarPorPorte(@PathVariable(name = "porte") final PorteEnum porte) {
        ConsumoAlimentoResposta consumoAlimento = consumoAlimentoService.buscarPorPorte(porte);
        return ResponseEntity.ok(consumoAlimento);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<ConsumoAlimentoResposta>> ListarConsumo() {
        List<ConsumoAlimentoResposta> consumoAlimentos = consumoAlimentoService.buscarTodos();
        return ResponseEntity.ok(consumoAlimentos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PutMapping
    public ResponseEntity<ConsumoAlimentoResposta> atualizarConsumo(@RequestBody @Valid final ConsumoAlimentoRequisicao consumoAlimento) {
        ConsumoAlimentoResposta entidadeAtualizada =  consumoAlimentoService.atualizar(consumoAlimento);
        return ResponseEntity.ok().body(entidadeAtualizada);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @DeleteMapping("/{porte}")
    public ResponseEntity<Void> deletarConsumo(@PathVariable(name="porte") final PorteEnum porte) {
        consumoAlimentoService.buscarPorPorte(porte);
        return ResponseEntity.noContent().build();
    }
}