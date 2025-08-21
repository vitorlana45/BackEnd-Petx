package org.ong.pet.pex.backendpetx.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.*;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalConjuntoDTO extends AnimalGenericoRequisicao {

    private Set<AnimalConjuntoDTO> animalConjunto;
    private Long id;

    // Removido construtor customizado que chamava super com assinatura antiga.
}
