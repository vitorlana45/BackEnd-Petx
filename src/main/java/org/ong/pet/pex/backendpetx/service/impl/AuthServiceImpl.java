package org.ong.pet.pex.backendpetx.service.impl;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.*;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResposta;
import org.ong.pet.pex.backendpetx.entities.*;
import org.ong.pet.pex.backendpetx.repositories.*;
import org.ong.pet.pex.backendpetx.security.TokenService;
import org.ong.pet.pex.backendpetx.service.*;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioNaoEncontrado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tempoExpiracaoToken;

    @Value("${email.password-recover.token.uri}")
    private String recuperarUri;

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final RecuperarSenhaRepository recuperarSenhaRepository;
    private final EmailService emailService;


    public AuthServiceImpl(TokenService tokenService, UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager, RecuperarSenhaRepository recuperarSenhaRepository, EmailService emailService, EmailService emailService1) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;

        this.recuperarSenhaRepository = recuperarSenhaRepository;
        this.emailService = emailService1;
    }

    @Transactional
    public AuthLoginResposta validarLogin(@Valid AuthLoginRequisicao data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        Usuario usuario = (Usuario) auth.getPrincipal();

        String token = this.tokenService.gerarToken(usuario);

        Long expirationDate = this.tokenService.pegarExpiracaoDoToken(token);

        return new AuthLoginResposta(token, expirationDate);
    }


    @Transactional
    public void criarRecuperarToken(@Valid EmailDTO emailDTO) {

        Usuario usuario = this.usuarioRepository.findUsuarioByEmail(emailDTO.email());
        if(usuario == null){
            throw new UsuarioNaoEncontrado("Email não encontrado");
        }
        RecuperarSenha entidade = new RecuperarSenha();
        entidade.setEmail(emailDTO.email());
        entidade.setToken(UUID.randomUUID().toString());
        entidade.setExpiracaoToken(Instant.now().plusSeconds(this.tempoExpiracaoToken * 60));
        entidade = recuperarSenhaRepository.save(entidade);

        String body = "acesse o link para redefinir uma nova senha\n\n" + this.recuperarUri + entidade.getToken();

        emailService.enviarEmail(entidade.getEmail(),"Recuperação de senha",body);

    }
}
