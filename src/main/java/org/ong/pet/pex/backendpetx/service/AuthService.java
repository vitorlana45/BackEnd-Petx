package org.ong.pet.pex.backendpetx.service;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.EmailDTO;
import org.ong.pet.pex.backendpetx.dto.request.NovaSenhaRequisicao;

public interface AuthService {

//    AuthLoginResposta validarLogin(@Valid AuthLoginRequisicao data);
    void criarRecuperarToken(@Valid EmailDTO emailDTO);
    void salvarNovaSenha(@Valid NovaSenhaRequisicao emailDTO);
}
