package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.ong.pet.pex.backendpetx.enums.ComportamentoEnum;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalGenericoRequisicao {

    @NotBlank(message = "O chipId não pode estar em branco e nem nulo")
    private String chipId;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @NotNull(message = "A idade é obrigatória")
    @Min(value = 0, message = "A idade não pode ser negativa")
    private int idade;

    @NotBlank(message = "A raça não pode estar em branco")
    private String raca;

    @NotNull(message = "O sexo é obrigatório")
    private SexoEnum sexo;

    @NotNull(message = "A origem é obrigatória")
    private OrigemAnimalEnum origem;

    @NotNull(message = "O porte é obrigatório")
    private PorteEnum porte;

    @NotNull(message = "O comportamento é obrigatório")
    private ComportamentoEnum comportamento;

    @NotNull(message = "A espécie é obrigatória")
    private EspecieEnum especie;

    @UniqueElements(message = "Doença repetida")
    private Set<String> doencas;

    @NotNull(message = "O status de vida é obrigatório")
    private boolean estaVivo;


}
