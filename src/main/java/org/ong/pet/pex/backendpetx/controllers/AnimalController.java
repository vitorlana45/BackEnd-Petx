package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
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

@Validated
@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @PostMapping("/primeiro/cadastro/conjunto")
    public ResponseEntity<AnimalGenericoResposta> cadastrarAnimal(@RequestBody @Valid AnimalDTO animalDTO) {
        AnimalGenericoResposta entidade = animalService.cadastrarAnimalComConjunto(animalDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/buscar/{id}").buildAndExpand(entidade.getId()).toUri();
        return ResponseEntity.created(uri).body(entidade);
    }
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<AnimalGenericoResposta> cadastrarAnimalSemConjunto(@RequestBody @Valid AnimalDTO animalSemConjuntoDTO) {
        var entidade = animalService.cadastrarAnimalSolo(animalSemConjuntoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/buscar/{id}").buildAndExpand(entidade.getId()).toUri();
        return ResponseEntity.created(uri).body(entidade);
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<AnimalGenericoResposta> buscarAnimalPorId(@PathVariable final Long id) {
        AnimalGenericoResposta entidade = animalService.buscarAnimalPorId(id);
        return ResponseEntity.ok().body(entidade);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<AnimalGenericoResposta> atualizarAnimal(@PathVariable final Long id, @RequestBody @Valid AnimalDTO animalSemConjuntoDTO) {
        AnimalGenericoResposta entidade = animalService.atualizarAnimal(id, animalSemConjuntoDTO);
        return ResponseEntity.ok().body(entidade);
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletarAnimal(@PathVariable final Long id) {
        animalService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
