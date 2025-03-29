package org.ong.pet.pex.backendpetx.controllers.auth;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AuthLoginRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.EmailDTO;
import org.ong.pet.pex.backendpetx.dto.request.NovaSenhaRequisicaoDTO;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResposta;
import org.ong.pet.pex.backendpetx.service.AuthService;
import org.ong.pet.pex.backendpetx.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResposta> login(@RequestBody @Valid AuthLoginRequisicao authLoginRequisicao) {
        var token = authService.validarLogin(authLoginRequisicao);
        return ResponseEntity.ok().body(token);
    }

    @PutMapping("/nova-senha")
    public ResponseEntity<Void> salvarNovaSenha(@RequestBody @Valid NovaSenhaRequisicaoDTO body) {
        authService.salvarNovaSenha(body);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recuperar-token")
    public ResponseEntity<Void> recuperarToken(@RequestBody @Valid EmailDTO emailDTO) {
        authService.criarRecuperarToken(emailDTO);
        return ResponseEntity.noContent().build();
    }

}
