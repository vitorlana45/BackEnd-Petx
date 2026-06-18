package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.BuscarTodosUsuariosResposta;
import org.ong.pet.pex.backendpetx.dto.response.BuscarUsuarioPadraoResposta;
import org.ong.pet.pex.backendpetx.dto.response.CriacaoUsuarioResposta;
import org.ong.pet.pex.backendpetx.entity.UserRole;
import org.ong.pet.pex.backendpetx.entity.Usuario;
import org.ong.pet.pex.backendpetx.repository.UsuarioRepository;
import org.ong.pet.pex.backendpetx.service.UsuarioService;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public CriacaoUsuarioResposta inserirUsuario(UsuarioDTO usuarioDTO) {

        if (usuarioRepository.findByEmail(usuarioDTO.email()) != null) {
            throw UsuarioException.usuarioJaCadastrado(usuarioDTO.email());
        }

        Usuario entidade = new Usuario();
        entidade.setEmail(usuarioDTO.email());
        entidade.setNome(usuarioDTO.name());

        entidade.setPassword(passwordEncoder.encode(usuarioDTO.password()));
        entidade.setRole(UserRole.COLABORADOR);

        entidade = usuarioRepository.save(entidade);

        return new CriacaoUsuarioResposta(entidade.getId(),entidade.getNome(), entidade.getEmail(), entidade.getRole());
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw UsuarioException.usuarioNaoEncontrado(id.toString());
        }
        try {
            usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Falha de integridade referencial");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public BuscarUsuarioPadraoResposta buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> UsuarioException.usuarioNaoEncontrado(id.toString()));
        return new BuscarUsuarioPadraoResposta(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole().toString());
    }


// FORMA PROVISORIA, VER DEPOIS QUAIS DADOS RETORNAR
    @Transactional(readOnly = true)
    @Override
    public List<BuscarTodosUsuariosResposta> buscarTodosUsuarios() {
        var list = usuarioRepository.findAll();

        return  list.stream().map(usuario -> new BuscarTodosUsuariosResposta
                (usuario.getId(), usuario.getNome(),usuario.getEmail(), usuario.getRole().toString())).toList();
    }



    @Transactional(readOnly = true)
    @Override
    public BuscarUsuarioPadraoResposta buscarUsuarioPorEmail(String email) {

      Usuario usuario = usuarioRepository.findUsuarioByEmail(email).orElseThrow(() -> UsuarioException.usuarioNaoEncontrado(email));

        return new BuscarUsuarioPadraoResposta(usuario.getId(),usuario.getNome(), usuario.getEmail(), usuario.getRole().toString());
    }
}
