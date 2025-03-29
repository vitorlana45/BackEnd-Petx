package org.ong.pet.pex.backendpetx.dto.response;

import lombok.Builder;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;
import java.util.Set;

@Builder
public record RespostaAnimalSemConjunto (
         Long id,
         String chipId,
         String nome,
         String maturidade,
         String raca,
         String sexo,
         String origem,
         String porte,
         String comportamento,
         String especie,
         Set<String> doencas,
         String status,
         String destino,
         String corPelagem,
         MaezinhaComFilhotesDTO maezinhaComFilhotes



){
}
