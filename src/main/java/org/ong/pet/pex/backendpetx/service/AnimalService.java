package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AnimalObituarioResquisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;

import java.util.List;
import java.util.Map;

public interface AnimalService {


    List<RespostaAnimalSemConjunto> listaAnimaisCadastrados ();

    void adicionarAdocaoConjuntaEmAnimal (Map<String,String> chips);

    AnimalGenericoResposta atualizarAnimal (Long id, AnimalGenericoRequisicao animalSemConjuntoDTO);

    void deletarPorId (Long id);

    void declararObito (AnimalObituarioResquisicao obiturario);

    AnimalGenericoResposta cadastrarAnimalSolo (AnimalGenericoRequisicao animalGenericoRequisicao);

    AnimalGenericoResposta buscarAnimalPorId(Long id);

    AnimalGenericoResposta buscarAnimalPorChip(String chip);

}
