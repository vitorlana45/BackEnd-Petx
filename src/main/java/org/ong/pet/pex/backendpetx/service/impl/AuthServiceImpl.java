package org.ong.pet.pex.backendpetx.service.impl;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AuthLoginRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.EmailDTO;
import org.ong.pet.pex.backendpetx.dto.request.NovaSenhaRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResposta;
import org.ong.pet.pex.backendpetx.entity.RecuperarSenha;
import org.ong.pet.pex.backendpetx.entity.Usuario;
import org.ong.pet.pex.backendpetx.repository.RecuperarSenhaRepository;
import org.ong.pet.pex.backendpetx.repository.UsuarioRepository;
import org.ong.pet.pex.backendpetx.service.AuthService;
import org.ong.pet.pex.backendpetx.service.exceptions.AuthException;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${email.password-recover.token.uri}")
    private String recuperarUri;

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final RecuperarSenhaRepository recuperarSenhaRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager, RecuperarSenhaRepository recuperarSenhaRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;

        this.recuperarSenhaRepository = recuperarSenhaRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void criarRecuperarToken(@Valid EmailDTO emailDTO) {

        Usuario usuario = this.usuarioRepository.findUsuarioByEmail(emailDTO.email()).orElseThrow(() -> new AuthException("Usuário não encontrado"));

        RecuperarSenha entidade = new RecuperarSenha();
        entidade.setEmail(emailDTO.email());
        entidade.setToken(UUID.randomUUID().toString());
//        entidade.setExpiracaoToken(Instant.now().plusSeconds(this.tempoExpiracaoToken * 60L));
        entidade = recuperarSenhaRepository.save(entidade);

        String body = "acesse o link para definir uma nova senha\n\n" + this.recuperarUri + entidade.getToken()
                + " validade de " + " minutos"; //this.tempoExpiracaoToken

        emailService.enviarEmail(entidade.getEmail(),"Recuperação de senha",body);

    }

    @Transactional
    public void salvarNovaSenha(NovaSenhaRequisicao dto) {

        List<RecuperarSenha> resultado = recuperarSenhaRepository.procurarTokensValidos(dto.token(), Instant.now());

        if(resultado.isEmpty()){
            throw new AuthException("Token de recuperação de senha inválido");
        }

        Usuario usuario = usuarioRepository.findUsuarioByEmail(resultado.getFirst().getEmail()).orElseThrow(() -> new AuthException("Usuário não encontrado"));
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuarioRepository.save(usuario);

    }
}
