package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;

public interface BoletimService {

    BoletimDTOResposta createBoletim(BoletimDTORequisicao dto);
    BoletimDTOResposta getBoletim(Long id);
    void deleteBoletim(Long id);
    BoletimDTOResposta updateBoletim(Long id, BoletimDTORequisicao dto);

}
