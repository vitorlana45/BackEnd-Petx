package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarTodosUsuarios;
import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarUsuarioPadrao;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;

import java.util.List;
import java.util.Map;

public interface UsuarioService {

    RespostaCricaoUsuario inserirUsuario(UsuarioDTO usuarioDTO);

    void deletarUsuario(Long id);

    RespostaBuscarUsuarioPadrao buscarUsuarioPorId(Long id);

    List<RespostaBuscarTodosUsuarios> buscarTodosUsuarios();

    RespostaBuscarUsuarioPadrao buscarUsuarioPorEmail(String email);

}
