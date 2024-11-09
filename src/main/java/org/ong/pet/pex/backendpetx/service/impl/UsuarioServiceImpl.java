package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.RespostaBuscarUsuarioPadrao;
import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;
import org.ong.pet.pex.backendpetx.entities.*;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.ong.pet.pex.backendpetx.service.UsuarioService;
import org.ong.pet.pex.backendpetx.service.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }



    @Override
    @Transactional
    public RespostaCricaoUsuario inserirUsuario(UsuarioDTO usuarioDTO) {

        if (usuarioRepository.findByEmail(usuarioDTO.email()) != null) {
            throw new UsuarioJaCadastrado("Usuário já cadastrado");
        }

        Usuario entidade = new Usuario();
        entidade.setEmail(usuarioDTO.email());

        String encryptedPassword = new BCryptPasswordEncoder().encode(usuarioDTO.password());

        entidade.setPassword(encryptedPassword);
        entidade.setRole(UserRole.USER);

        entidade = usuarioRepository.save(entidade);

        return new RespostaCricaoUsuario(entidade.getId(),entidade.getEmail(), entidade.getRole());
    }




    @Override
    public UsuarioDTO atualizarUsuario(UsuarioDTO usuarioDTO) {
        return null;
    }



    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontrado("Usuário não encontrado");
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
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontrado("Usuário não encontrado"));
        return new RespostaBuscarUsuarioPadrao(usuario.getId(), usuario.getEmail());
    }



    @Transactional(readOnly = true)
    @Override
    public  Map<String, String>  buscarTodosUsuarios() {
        var a = usuarioRepository.findAll();
        Map<String, String> map = new HashMap<>();

        for (Usuario usuario : a) {
            map.put(usuario.getEmail(), usuario.getRole().toString());
        }
        return map;
    }




    @Override
    public RespostaBuscarUsuarioPadrao buscarUsuarioPorEmail(String email) {

      Usuario usuario = usuarioRepository.findUsuarioByEmail(email);

      if(usuario == null) {
        throw new UsuarioNaoEncontrado("Usuário não encontrado");
      }
        return new RespostaBuscarUsuarioPadrao(usuario.getId(), usuario.getEmail());
    }
}
