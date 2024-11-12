package org.ong.pet.pex.backendpetx.dto.response;

import lombok.Getter;

import java.util.Set;

@Getter
public class RespostaAnimalSemConjunto extends AnimalGenericoResposta {

    public RespostaAnimalSemConjunto(Long id, String nome, Integer idade, String raca, String sexo, String origem,
                                     String porte, String comportamento, String especie, Set<String> doencas, boolean estaVivo,Set<AnimalGenericoResposta> animalConjunto) {
        super(id, nome, idade, raca, sexo, origem, porte, comportamento, especie, doencas, estaVivo, animalConjunto);
    }
}
