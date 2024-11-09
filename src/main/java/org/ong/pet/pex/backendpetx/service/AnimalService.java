package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;

import java.util.List;

public interface AnimalService {


    List<AnimalDTO> animaisCadastrados ();

    AnimalDTO cadastrarAnimalComConjunto (AnimalDTO animalDTO);

    AnimalDTO atualizarAnimal (Long id);

    void deletarPorId (Long id);

    void declararObito (Long id);

}
