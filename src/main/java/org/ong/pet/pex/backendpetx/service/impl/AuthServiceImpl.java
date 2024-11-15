package org.ong.pet.pex.backendpetx.service.impl;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AuthLoginRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.EmailDTO;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResposta;
import org.ong.pet.pex.backendpetx.entities.RecuperarSenha;
import org.ong.pet.pex.backendpetx.entities.Usuario;
import org.ong.pet.pex.backendpetx.repositories.RecuperarSenhaRepository;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.ong.pet.pex.backendpetx.security.TokenService;
import org.ong.pet.pex.backendpetx.service.AuthService;
import org.ong.pet.pex.backendpetx.service.EmailService;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

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

        Authentication auth = null;

        try {
            System.out.println("antes");
            auth = this.authenticationManager.authenticate(usernamePassword);
        } catch (RuntimeException ex) {
            UsuarioException.usuarioErroFazerLogin("Credenciais inválidas. Verifique o e-mail e senha informados.");
        }

        // Verifica se o usuário foi autenticado
        Usuario usuario = (Usuario) auth.getPrincipal();
        if (usuario == null) {
            UsuarioException.usuarioErroFazerLogin("Error: Verifique suas credenciais");
        }

        // Gera o token e sua data de expiração
        String token = this.tokenService.gerarToken(usuario);
        Long expirationDate = this.tokenService.pegarExpiracaoDoToken(token);

        // Retorna a resposta de login
        return new AuthLoginResposta(token, expirationDate);
    }


    @Transactional
    public void criarRecuperarToken(@Valid EmailDTO emailDTO) {

        Usuario usuario = this.usuarioRepository.findUsuarioByEmail(emailDTO.email());
        if(usuario == null){
            throw UsuarioException.usuarioJaCadastrado(emailDTO.email());
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
