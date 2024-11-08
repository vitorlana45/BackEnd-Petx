package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;
import org.ong.pet.pex.backendpetx.entities.*;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioJaCadastrado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDTO.email());
        usuario.setSenha(usuarioDTO.senha());
        usuario.setRole(UserRole.USER);

        usuario = usuarioRepository.save(usuario);

        return new RespostaCricaoUsuario(usuario.getEmail(), usuario.getRole());
    }

    @Override
    public UsuarioDTO atualizarUsuario(UsuarioDTO usuarioDTO) {
        return null;
    }

    @Override
    public void deletarUsuario(Long id) {

    }

    @Override
    public UsuarioDTO buscarUsuarioPorId(Long id) {
        return null;
    }

    @Override
    public List<UsuarioDTO> buscarTodosUsuarios() {
        return List.of();
    }

    @Override
    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        return null;
    }
}
