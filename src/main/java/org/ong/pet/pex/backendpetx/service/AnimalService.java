package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;

import java.util.List;

public interface AnimalService {


    List<AnimalDTO> animaisCadastrados ();

    AnimalGenericoResposta cadastrarAnimalComConjunto (AnimalDTO animalDTO);

    AnimalGenericoResposta atualizarAnimal (Long id, AnimalDTO animalSemConjuntoDTO);

    void deletarPorId (Long id);

    void declararObito (Long id);

    AnimalGenericoResposta cadastrarAnimalSolo (AnimalGenericoRequisicao animalGenericoRequisicao);

    AnimalGenericoResposta buscarAnimalPorId(Long id);

}
