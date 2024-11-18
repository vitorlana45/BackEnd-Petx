package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("VACINA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vacina extends Produto {

    private EspecieEnum especie;
    private PorteEnum porte;
    private String lote;
    private LocalDate dataValidade;
}