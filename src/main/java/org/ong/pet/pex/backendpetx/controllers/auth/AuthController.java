package org.ong.pet.pex.backendpetx.controllers.auth;

import org.ong.pet.pex.backendpetx.dto.request.AuthLoginRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResposta;
import org.ong.pet.pex.backendpetx.service.impl.AuthServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;


    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthLoginResposta login(@RequestBody AuthLoginRequisicao authLoginRequisicao) {
        return authService.validateLogin(authLoginRequisicao);
    }

}
