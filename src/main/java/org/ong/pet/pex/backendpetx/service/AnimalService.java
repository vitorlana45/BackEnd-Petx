package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.*;
import org.ong.pet.pex.backendpetx.dto.response.*;

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
