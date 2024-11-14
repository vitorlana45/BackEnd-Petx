package org.ong.pet.pex.backendpetx.dto.response;

import java.util.Set;

public record RespostaAnimalSemConjunto (
         Long id,
         String chipId,
         String nome,
         int idade,
         String raca,
         String sexo,
         String origem,
         String porte,
         String comportamento,
         String especie,
         Set<String> doencas,
         boolean estaVivo


){
}
