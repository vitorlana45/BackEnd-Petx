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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("MEDICAMENTO")
public class Medicamento extends Produto {

    private String principioAtivo;
    private String fabricante;
    private LocalDate dataValidade;
    private PorteEnum porte;
    private EspecieEnum especie;

}