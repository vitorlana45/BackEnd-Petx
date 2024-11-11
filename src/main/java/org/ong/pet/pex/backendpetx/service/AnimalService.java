package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalSemConjuntoDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalComConjuntoDTO;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;

import java.util.List;

public interface AnimalService {


    List<AnimalDTO> animaisCadastrados ();

    RespostaAnimalComConjuntoDTO cadastrarAnimalComConjunto (AnimalDTO animalDTO);

    AnimalDTO atualizarAnimal (Long id);

    void deletarPorId (Long id);

    void declararObito (Long id);

    RespostaAnimalSemConjunto animalSemConjunto (AnimalSemConjuntoDTO animalSemConjuntoDTO);

    RespostaAnimalComConjuntoDTO buscarAnimalPorId(Long id);

}
