package org.ong.pet.pex.backendpetx.controllers;

import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.service.BoletimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/boletim")
public class BoletimController {

    @Autowired
    private final BoletimService boletimService;

    public BoletimController(BoletimService boletimService) {
        this.boletimService = boletimService;
    }


    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<BoletimDTOResposta> createBoletim(@RequestBody BoletimDTORequisicao dto) {
        BoletimDTOResposta saved = boletimService.createBoletim(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BoletimDTOResposta> getBoletim(@PathVariable Long id) {
        BoletimDTOResposta boletim = boletimService.getBoletim(id);
        return ResponseEntity.ok(boletim);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @PatchMapping   ("/{id}")
    public ResponseEntity<BoletimDTOResposta> updateBoletim(@PathVariable Long id, @RequestBody BoletimDTORequisicao dto) {
        BoletimDTOResposta updated = boletimService.updateBoletim(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<BoletimDTOResposta>> findAllBoletins(
            @RequestParam(required = false) Long numeroOcorrencia,
            @RequestParam(required = false) Destino destino,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            Pageable pageable
    ) {
        System.out.println("numeroOcorrencia: " + numeroOcorrencia);
//        System.out.println("dataAtendimento: " + dataAtendimento);
        System.out.println("pageable: " + pageable);

        Page<BoletimDTOResposta> boletins = boletimService.findAllBoletins(
                numeroOcorrencia,
//                dataInicio,
//                dataFim,
                destino,
                pageable);
        return ResponseEntity.ok(boletins);
    }

    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoletim(@PathVariable Long id) {
        boletimService.deleteBoletim(id);
        return ResponseEntity.noContent().build();
    }
}