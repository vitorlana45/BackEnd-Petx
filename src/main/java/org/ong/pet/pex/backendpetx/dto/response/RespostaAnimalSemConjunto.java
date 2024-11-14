package org.ong.pet.pex.backendpetx.dto.response;

import java.util.List;

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
         List<String> doencas,
         boolean estaVivo


){
}
