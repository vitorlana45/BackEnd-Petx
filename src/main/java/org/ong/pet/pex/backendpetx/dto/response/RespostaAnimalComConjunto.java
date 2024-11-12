package org.ong.pet.pex.backendpetx.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RespostaAnimalComConjunto extends AnimalGenericoResposta {

    public RespostaAnimalComConjunto(Long id, String nome, Integer idade, String raca, String string, String string1, String string2, String string3, String string4, Set<String> doencas, boolean estaVivo, Set<AnimalGenericoResposta> animalConjunto) {
        super(id, nome, idade, raca, string, string1, string2, string3, string4, doencas, estaVivo,animalConjunto);
    }
}
