package org.ong.pet.pex.backendpetx.controllers.auth;

import org.ong.pet.pex.backendpetx.dto.request.AuthLoginRequest;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResponse;
import org.ong.pet.pex.backendpetx.service.*;
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
    public String login(@RequestBody AuthLoginRequest authLoginResponse) {
        return authService.validateLogin(authLoginResponse);
    }

}
