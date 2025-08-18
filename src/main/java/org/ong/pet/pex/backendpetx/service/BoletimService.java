package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;

public interface BoletimService {

    BoletimDTOResposta createBoletim(BoletimDTORequisicao dto);
    BoletimDTOResposta getBoletim(Long id);
    void deleteBoletim(Long id);
    BoletimDTOResposta updateBoletim(Long id, BoletimDTORequisicao dto);
    Page<BoletimDTOResposta> findAllBoletins(
            Long numeroOcorrencia,
//            LocalDateTime dataInicio,
//            LocalDateTime dataFim,
            Destino destino,
            Pageable pageable
    );

    Map<String, Object> obterEstatisticasResgates(int mesesSérieTemporal);
}
