package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;
import org.ong.pet.pex.backendpetx.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/inserir")
    public ResponseEntity<RespostaCricaoUsuario> inserirUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("api/buscar/usuario/{id}").buildAndExpand(usuarioDTO.email()).toUri();
        return ResponseEntity.created(uri).body(usuarioService.inserirUsuario(usuarioDTO));
    }

}
