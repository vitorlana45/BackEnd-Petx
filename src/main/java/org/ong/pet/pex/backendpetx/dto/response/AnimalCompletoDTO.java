package org.ong.pet.pex.backendpetx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.dto.request.AnimalConjuntoDTO;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AnimalCompletoDTO extends AnimalGenericoResposta {

    private Set<AnimalConjuntoDTO> animalConjunto;

}
