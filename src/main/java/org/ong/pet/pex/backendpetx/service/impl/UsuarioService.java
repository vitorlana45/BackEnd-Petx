package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;

import java.util.List;

public interface UsuarioService {

    RespostaCricaoUsuario inserirUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO atualizarUsuario(UsuarioDTO usuarioDTO);

    void deletarUsuario(Long id);

    UsuarioDTO buscarUsuarioPorId(Long id);

    List<UsuarioDTO> buscarTodosUsuarios();

    UsuarioDTO buscarUsuarioPorEmail(String email);

}
