package org.ong.pet.pex.backendpetx.controllers.api;

import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/animais")
public class AnimalRestController {

    private final AnimalService animalService;

    public AnimalRestController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalGenericoResposta> buscarAnimal(@PathVariable Long id) {
        try {
            AnimalGenericoResposta animal = animalService.buscarAnimalPorId(id);
            return ResponseEntity.ok(animal);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/detalhes")
    public ResponseEntity<Map<String, Object>> buscarDetalhesAnimal(@PathVariable Long id) {
        try {
            AnimalGenericoResposta animal = animalService.buscarAnimalPorId(id);
            
            Map<String, Object> detalhes = new HashMap<>();
            
            // Adicionar doenças (se disponíveis no DTO)
            if (animal.getDoencas() != null) {
                detalhes.put("doencas", animal.getDoencas());
            } else {
                detalhes.put("doencas", Collections.emptyList());
            }
            
            // Adicionar comportamento (como lista para manter consistência com frontend)
            if (animal.getComportamento() != null && !animal.getComportamento().isEmpty()) {
                detalhes.put("comportamentos", Collections.singletonList(animal.getComportamento()));
            } else {
                detalhes.put("comportamentos", Collections.emptyList());
            }
            
            return ResponseEntity.ok(detalhes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
