package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService animalService;


    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/primeiro/cadastro/conjunto")
    public AnimalDTO cadastrarAnimal(@RequestBody @Valid AnimalDTO animalDTO) {
        return animalService.cadastrarAnimalComConjunto(animalDTO);
    }


}
