package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResponse;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

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

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/todos")
    public ResponseEntity<Set<TutorDTOResponse>> buscarTodosTutores() {
        return ResponseEntity.ok(tutorService.buscarTodosTutores());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/{cpf}")
    public ResponseEntity<TutorDTOResponse> buscarTutorPorCpf(@PathVariable(name = "cpf") String cpf) {
        return ResponseEntity.ok(tutorService.buscarTutorPorCpf(cpf));
    }

    @PatchMapping("/{cpf}")
    public ResponseEntity<HttpStatus> atualizarDadosTutor(@RequestBody @Valid AtualizarTutorRequisicao atualizarTutorRequisicao, @PathVariable(name = "cpf") String cpfAntigo) {
        var cpf = tutorService.atualizarDadosTutor(cpfAntigo, atualizarTutorRequisicao);
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/tutor/{cpf}").buildAndExpand(cpf).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "http://localhost:8080/api/tutor/" + cpf);
        return ResponseEntity.noContent().headers(headers).build();
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<HttpStatus> deletarTutor(@PathVariable(name = "cpf") String cpf) {
        tutorService.deletarTutorPorCpf(cpf);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> buscarTutorPorId(@PathVariable(name = "id") Long id) {
        tutorService.deletarTutorPorId(id);
        return ResponseEntity.noContent().build();
    }
}












