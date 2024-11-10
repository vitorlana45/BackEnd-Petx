package org.ong.pet.pex.backendpetx.controllers.auth;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.*;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResposta;
import org.ong.pet.pex.backendpetx.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;


    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResposta> login(@RequestBody AuthLoginRequisicao authLoginRequisicao) {
        var token = authService.validarLogin(authLoginRequisicao);
        return ResponseEntity.ok().body(token);
    }

    @PutMapping("/recuperar-token")
    public ResponseEntity<Void> recoverToken(@RequestBody @Valid EmailDTO emailDTO) {
        authService.criarRecuperarToken(emailDTO);
        return ResponseEntity.noContent().build();
    }

}
