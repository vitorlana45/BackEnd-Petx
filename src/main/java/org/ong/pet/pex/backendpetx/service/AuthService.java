package org.ong.pet.pex.backendpetx.service;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AuthLoginRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.EmailDTO;
import org.ong.pet.pex.backendpetx.dto.response.AuthLoginResposta;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioNaoEncontrado;

public interface AuthService {

    AuthLoginResposta validateLogin(@Valid AuthLoginRequisicao data) throws UsuarioNaoEncontrado;


    void criarRecuperarToken(@Valid EmailDTO emailDTO) throws UsuarioNaoEncontrado;
}
