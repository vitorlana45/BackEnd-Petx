package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;
import org.ong.pet.pex.backendpetx.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/inserir")
    public ResponseEntity<RespostaCricaoUsuario> inserirUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {

        RespostaCricaoUsuario usuarioCriado = usuarioService.inserirUsuario(usuarioDTO);

        // Obter o ID do usuário recém-criado
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(usuarioCriado.id()).toUri();  // Usar o ID gerado

        // Retornar a resposta com o código 201 e o URI
        return ResponseEntity.created(uri).body(usuarioCriado);
    }
}
