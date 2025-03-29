package org.ong.pet.pex.backendpetx.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.*;

import java.util.Set;


@Getter
@Setter
public class AnimalConjuntoDTO extends AnimalGenericoRequisicao {

    private Set<AnimalConjuntoDTO> animalConjunto;
    private Long id;


    public AnimalConjuntoDTO(String chipId, Long id, String nome, MaturidadeEnum
            maturidade, String raca, SexoEnum sexo, OrigemAnimalEnum origem, PorteEnum porte, Destino destino, String comportamento, String corPelagem, EspecieEnum especie, Set<String> doencas, boolean animalEMaezinha, MaezinhaComFilhotesDTO maezinhaComFilhotesDTO, String condicaoAnimal, StatusEnum statusEnum) {
        super(chipId, nome, raca ,maturidade, sexo, origem, porte, destino, comportamento,corPelagem, especie, doencas, animalEMaezinha, maezinhaComFilhotesDTO, condicaoAnimal, statusEnum);
        this.id = id;
    }

}
