package org.ong.pet.pex.backendpetx.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.service.validation.MaeFilhotesConsistente;

import java.util.Set;


@Getter
@Setter
@MaeFilhotesConsistente
public class AnimalDTO extends AnimalGenericoRequisicao {
        private Set<AnimalConjuntoDTO> animalConjunto;
}