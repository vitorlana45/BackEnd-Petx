package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedida;

@Entity
@DiscriminatorValue("RACAO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Racao extends Produto {

    private String marca;
    private EspecieEnum especie;
    private PorteEnum porte;
    private UnidadeDeMedida unidadeMedida;

}
