package org.ong.pet.pex.backendpetx.dto.response;

import lombok.*;

import java.util.Set;
public record RespostaAnimalComConjuntoDTO(
        Long id,
        String nome,
        Integer idade,
        String raca,
        String sexo,
        String origem,
        String porte,
        String comportamento,
        String especie,
        Set<RespostaAnimalComConjuntoDTO> animalConjunto
) {

}
