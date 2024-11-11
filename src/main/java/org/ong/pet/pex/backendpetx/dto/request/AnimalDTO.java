package org.ong.pet.pex.backendpetx.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class AnimalDTO extends AnimalGenericoRequisicao {

        private Set<AnimalConjuntoDTO> animalConjunto;

}