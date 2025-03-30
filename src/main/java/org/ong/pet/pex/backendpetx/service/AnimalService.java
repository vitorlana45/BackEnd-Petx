package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AnimalObituarioResquisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.AnimalPaginadoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;
import org.ong.pet.pex.backendpetx.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AnimalService {

    void adicionarAdocaoConjuntaEmAnimal (Map<String,Long> ids);

    AnimalGenericoResposta atualizarAnimal (Long id, AnimalGenericoRequisicao animalSemConjuntoDTO);

    void deletarPorId (Long id);

    void declararObito (AnimalObituarioResquisicao obiturario);

    AnimalGenericoResposta buscarAnimalPorId(Long id);

    AnimalGenericoResposta buscarAnimalPorChip(String chip);

    Page<AnimalPaginadoResposta> paginarAnimais(String nome, String raca, EspecieEnum especie, PorteEnum porte, StatusEnum status,
                                                String doenca, String comportamento, MaturidadeEnum maturidade,
                                                OrigemAnimalEnum origem, SexoEnum sexo, Pageable pageable);
}
