package org.ong.pet.pex.backendpetx.service.impl;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AuthLoginRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResposta;
import org.ong.pet.pex.backendpetx.entities.Usuario;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.ong.pet.pex.backendpetx.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AuthServiceImpl {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(TokenService tokenService, UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthLoginResposta validateLogin(@Valid AuthLoginRequisicao data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        // Extraindo o usuário autenticado
        Usuario usuario = (Usuario) auth.getPrincipal();

        // Gerando o token
        String token = this.tokenService.gerarToken(usuario);

        // Obtendo o tempo de expiração do token
        Long expirationDate = this.tokenService.pegarExpiracaoDoToken(token);

        // Retornando os dados com o token e o tempo de expiração
        return new AuthLoginResposta(token, expirationDate);
    }

}
