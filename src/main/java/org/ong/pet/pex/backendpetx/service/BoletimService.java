package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.BoletimRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimResposta;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;

import java.util.Map;
import java.time.LocalDateTime;
import java.util.List;

public interface BoletimService {

    BoletimResposta createBoletim(BoletimRequisicao dto);
    BoletimResposta getBoletim(Long id);
    void deleteBoletim(Long id);
    BoletimResposta updateBoletim(Long id, BoletimRequisicao dto);

    /**
     * Adiciona um novo animal a um boletim/ocorrência existente sem duplicar o número.
     */
    BoletimResposta adicionarAnimalEmOcorrencia(Long numeroOcorrencia, AnimalGenericoRequisicao animal);

    /**
     * Verifica se uma ocorrência existe e pertence à ONG informada.
     */
    boolean existsNumeroOcorrenciaNaOng(Long numeroOcorrencia, Long ongId);

    /**
     * Busca ocorrências da ONG para facilitar o vínculo (filtros opcionais).
     */
    List<BoletimResposta> buscarOcorrenciasParaVinculo(Long ongId,
                                                         LocalDateTime inicio,
                                                         LocalDateTime fim,
                                                         OrigemAnimalEnum origem,
                                                         Destino destino,
                                                         int limit);
    Page<BoletimResposta> findAllBoletins(
            Long numeroOcorrencia,
//            LocalDateTime dataInicio,
//            LocalDateTime dataFim,
            Destino destino,
            Pageable pageable
    );

    Map<String, Object> obterEstatisticasResgates(int mesesSérieTemporal);

    long contarTotalBoletins();

}
