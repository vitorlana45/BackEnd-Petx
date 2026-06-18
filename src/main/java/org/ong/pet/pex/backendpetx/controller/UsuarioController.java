package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarTodosUsuarios;
import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarUsuarioPadrao;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;
import org.ong.pet.pex.backendpetx.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/registrar")
    public ResponseEntity<RespostaCricaoUsuario> inserirUsuario(@RequestBody @Valid final UsuarioDTO usuarioDTO) {

        RespostaCricaoUsuario usuarioCriado = usuarioService.inserirUsuario(usuarioDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuarioCriado.id()).toUri();
        return ResponseEntity.created(uri).body(usuarioCriado);
    }


    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RespostaBuscarUsuarioPadrao> buscarUsuarioPorId(@PathVariable final Long id) {
        RespostaBuscarUsuarioPadrao usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }


    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/buscar/{email}")
    public ResponseEntity<RespostaBuscarUsuarioPadrao> buscarUsuarioPorEmail(@PathVariable(name = "email") final String email) {

        RespostaBuscarUsuarioPadrao usuario = usuarioService.buscarUsuarioPorEmail(email);

        return ResponseEntity.ok().body(usuario);
    }


    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<List<RespostaBuscarTodosUsuarios>> buscarTodosUsuarios() {
        return ResponseEntity.ok(usuarioService.buscarTodosUsuarios());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable final Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
