package org.ong.pet.pex.backendpetx.controller;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.BuscarTodosUsuariosResposta;
import org.ong.pet.pex.backendpetx.dto.response.BuscarUsuarioPadraoResposta;
import org.ong.pet.pex.backendpetx.dto.response.CriacaoUsuarioResposta;
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
    public ResponseEntity<CriacaoUsuarioResposta> inserirUsuario(@RequestBody @Valid final UsuarioDTO usuarioDTO) {

        CriacaoUsuarioResposta usuarioCriado = usuarioService.inserirUsuario(usuarioDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuarioCriado.id()).toUri();
        return ResponseEntity.created(uri).body(usuarioCriado);
    }


    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BuscarUsuarioPadraoResposta> buscarUsuarioPorId(@PathVariable final Long id) {
        BuscarUsuarioPadraoResposta usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }


    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/buscar/{email}")
    public ResponseEntity<BuscarUsuarioPadraoResposta> buscarUsuarioPorEmail(@PathVariable(name = "email") final String email) {

        BuscarUsuarioPadraoResposta usuario = usuarioService.buscarUsuarioPorEmail(email);

        return ResponseEntity.ok().body(usuario);
    }


    @PreAuthorize("hasAnyRole('COLABORADOR','ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<List<BuscarTodosUsuariosResposta>> buscarTodosUsuarios() {
        return ResponseEntity.ok(usuarioService.buscarTodosUsuarios());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable final Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
