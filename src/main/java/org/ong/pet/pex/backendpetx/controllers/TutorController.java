package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tutor")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping
    public ResponseEntity<HttpStatus> cadastrarTutor(@RequestBody @Valid CadastrarTutorRequisicao cadastrarTutorRequisicao) {
        var dataUri = tutorService.cadastrarTutor(cadastrarTutorRequisicao);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tutor/{id}").buildAndExpand(dataUri).toUri();
       return ResponseEntity.created(uri).build();
    }
}
