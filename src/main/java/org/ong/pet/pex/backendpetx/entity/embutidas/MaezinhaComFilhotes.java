package org.ong.pet.pex.backendpetx.entities.incorporarEntidades;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaezinhaComFilhotes {

    private int quantidadeMachos;
    private int quantidadeFemeas;

}
