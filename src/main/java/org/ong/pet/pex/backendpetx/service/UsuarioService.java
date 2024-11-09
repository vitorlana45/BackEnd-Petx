package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.RespostaBuscarUsuarioPadrao;
import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaCricaoUsuario;

import java.util.*;

public interface UsuarioService {

    RespostaCricaoUsuario inserirUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO atualizarUsuario(UsuarioDTO usuarioDTO);

    void deletarUsuario(Long id);

    RespostaBuscarUsuarioPadrao buscarUsuarioPorId(Long id);

    Map<String, String> buscarTodosUsuarios();

    RespostaBuscarUsuarioPadrao buscarUsuarioPorEmail(String email);

}
