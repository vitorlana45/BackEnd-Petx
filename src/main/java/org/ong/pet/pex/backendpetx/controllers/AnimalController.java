package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalSemConjuntoDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalComConjuntoDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<RespostaAnimalComConjuntoDTO> cadastrarAnimal(@RequestBody @Valid AnimalDTO animalDTO) {
        RespostaAnimalComConjuntoDTO entidade = animalService.cadastrarAnimalComConjunto(animalDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/buscar/{id}").buildAndExpand(entidade.id()).toUri();
        return ResponseEntity.created(uri).body(entidade);
    }
    @PreAuthorize("hasRole('COLABORADOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<RespostaAnimalSemConjunto> cadastrarAnimalSemConjunto(@RequestBody @Valid AnimalSemConjuntoDTO animalSemConjuntoDTO) {
        var entidade = animalService.animalSemConjunto(animalSemConjuntoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/buscar/{id}").buildAndExpand(entidade.id()).toUri();
        return ResponseEntity.created(uri).body(entidade);
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<RespostaAnimalComConjuntoDTO> buscarAnimalPorId(@PathVariable final Long id) {
        var entidade = animalService.buscarAnimalPorId(id);
        return ResponseEntity.ok().body(entidade);
    }

}
