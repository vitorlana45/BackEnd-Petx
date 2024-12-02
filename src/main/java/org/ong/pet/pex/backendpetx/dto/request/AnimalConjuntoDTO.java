package org.ong.pet.pex.backendpetx.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.ComportamentoEnum;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.MaturidadeEnum;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusEnum;

import java.util.Set;


@Getter
@Setter
public class AnimalConjuntoDTO extends AnimalGenericoRequisicao {

    private Set<AnimalConjuntoDTO> animalConjunto;
    private Long id;


    public AnimalConjuntoDTO(String chipId, Long id, String nome, MaturidadeEnum maturidade, String raca, SexoEnum sexo, OrigemAnimalEnum origem, PorteEnum porte, ComportamentoEnum comportamento, EspecieEnum especie, Set<String> doencas, StatusEnum statusEnum) {
        super(chipId, nome, raca ,maturidade, sexo, origem, porte, comportamento, especie, doencas, statusEnum);
        this.id = id;
    }

}
