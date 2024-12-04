package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AnimalObituarioResquisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @GetMapping("/conjunto/{principal}/{animal1}/{animal2}")
    public ResponseEntity<Void> adicionarAdocaoConjuntaEmAnimal(@PathVariable(name = "principal") final String principal,
                                                                @PathVariable(name = "animal1") final String animal1,
                                                                @PathVariable(required = false) final String animal2) {
        Map<String, String> chips = new HashMap<>();
        chips.put("principal", principal);
        chips.put("animal1", animal1);

        if (animal2 != null) chips.put("animal2", animal2);

        animalService.adicionarAdocaoConjuntaEmAnimal(chips);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @PostMapping()
    public ResponseEntity<AnimalGenericoResposta> cadastrarAnimalSemConjunto(@RequestBody @Valid final AnimalDTO animalSemConjuntoDTO) {
        var entidade = animalService.cadastrarAnimalSolo(animalSemConjuntoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entidade.getId()).toUri();
        return ResponseEntity.created(uri).body(entidade);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AnimalGenericoResposta> buscarAnimalPorId(@PathVariable final Long id) {
        AnimalGenericoResposta entidade = animalService.buscarAnimalPorId(id);
        return ResponseEntity.ok().body(entidade);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/chip/{chip}")
    public ResponseEntity<AnimalGenericoResposta> buscarAnimalPorChip(@PathVariable final String chip) {
        AnimalGenericoResposta entidade = animalService.buscarAnimalPorChip(chip);
        return ResponseEntity.ok().body(entidade);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AnimalGenericoResposta> atualizarAnimal(@PathVariable final Long id, @RequestBody @Valid final AnimalGenericoRequisicao animalSemConjuntoDTO) {
        AnimalGenericoResposta entidade = animalService.atualizarAnimal(id, animalSemConjuntoDTO);
        return ResponseEntity.ok().body(entidade);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAnimal(@PathVariable final Long id) {
        animalService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<List<RespostaAnimalSemConjunto>> listarAnimaisCadastrados() {
        List<RespostaAnimalSemConjunto> lista = animalService.listaAnimaisCadastrados();
        return ResponseEntity.ok().body(lista);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @PostMapping("/obito")
    public ResponseEntity<Void> declararObito(@RequestBody @Valid final AnimalObituarioResquisicao obiturario) {
        animalService.declararObito(obiturario);
        return ResponseEntity.noContent().build();
    }
}
