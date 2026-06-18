package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.UsuarioDTO;
import org.ong.pet.pex.backendpetx.dto.response.BuscarTodosUsuariosResposta;
import org.ong.pet.pex.backendpetx.dto.response.BuscarUsuarioPadraoResposta;
import org.ong.pet.pex.backendpetx.dto.response.CriacaoUsuarioResposta;

import java.util.List;

public interface UsuarioService {

    CriacaoUsuarioResposta inserirUsuario(UsuarioDTO usuarioDTO);

    void deletarUsuario(Long id);

    BuscarUsuarioPadraoResposta buscarUsuarioPorId(Long id);

    List<BuscarTodosUsuariosResposta> buscarTodosUsuarios();

    BuscarUsuarioPadraoResposta buscarUsuarioPorEmail(String email);

}
