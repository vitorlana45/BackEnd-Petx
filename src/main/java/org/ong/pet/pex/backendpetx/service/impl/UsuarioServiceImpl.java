package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarTodosUsuarios;
import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarUsuarioPadrao;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;
import org.ong.pet.pex.backendpetx.entities.UserRole;
import org.ong.pet.pex.backendpetx.entities.Usuario;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
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
    public RespostaCricaoUsuario inserirUsuario(UsuarioDTO usuarioDTO) {

        if (usuarioRepository.findByEmail(usuarioDTO.email()) != null) {
           UsuarioException.usuarioJaCadastrado(usuarioDTO.email());
        }

        Usuario entidade = new Usuario();
        entidade.setEmail(usuarioDTO.email());

        entidade.setPassword(passwordEncoder.encode(usuarioDTO.password()));
        entidade.setRole(UserRole.COLABORADOR);

        entidade = usuarioRepository.save(entidade);

        return new RespostaCricaoUsuario(entidade.getId(),entidade.getEmail(), entidade.getRole());
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
    public RespostaBuscarUsuarioPadrao buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> UsuarioException.usuarioNaoEncontrado(id.toString()));
        return new RespostaBuscarUsuarioPadrao(usuario.getId(), usuario.getEmail());
    }


// FORMA PROVISORIA, VER DEPOIS QUAIS DADOS RETORNAR
    @Transactional(readOnly = true)
    @Override
    public List<RespostaBuscarTodosUsuarios> buscarTodosUsuarios() {
        var list = usuarioRepository.findAll();

        return  list.stream().map(usuario -> new RespostaBuscarTodosUsuarios
                (usuario.getEmail(), usuario.getRole().toString())).toList();
    }



    @Transactional(readOnly = true)
    @Override
    public RespostaBuscarUsuarioPadrao buscarUsuarioPorEmail(String email) {

      Usuario usuario = usuarioRepository.findUsuarioByEmail(email);

      if(usuario == null) {
        throw UsuarioException.usuarioNaoEncontrado(email);
      }
        return new RespostaBuscarUsuarioPadrao(usuario.getId(), usuario.getEmail());
    }
}
