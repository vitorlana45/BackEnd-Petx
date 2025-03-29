package org.ong.pet.pex.backendpetx.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaezinhaComFilhotesDTO {

    private Integer quantidadeMacho;
    private Integer quantidadeFemea;

}