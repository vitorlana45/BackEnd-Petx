package org.ong.pet.pex.backendpetx.controllers;

import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.service.BoletimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boletins")
public class BoletimController {

    @Autowired
    private final BoletimService boletimService;

    public BoletimController(BoletimService boletimService) {
        this.boletimService = boletimService;
    }


    @PostMapping
    public ResponseEntity<BoletimDTOResposta> createBoletim(@RequestBody BoletimDTORequisicao dto) {
        BoletimDTOResposta saved = boletimService.createBoletim(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletimDTOResposta> getBoletim(@PathVariable Long id) {
        BoletimDTOResposta boletim = boletimService.getBoletim(id);
        return ResponseEntity.ok(boletim);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoletimDTOResposta> updateBoletim(@PathVariable Long id, @RequestBody BoletimDTORequisicao dto) {
        BoletimDTOResposta updated = boletimService.updateBoletim(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoletim(@PathVariable Long id) {
        boletimService.deleteBoletim(id);
        return ResponseEntity.noContent().build();
    }
}