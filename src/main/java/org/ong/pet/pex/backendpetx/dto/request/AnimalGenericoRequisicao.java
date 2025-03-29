package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ong.pet.pex.backendpetx.enums.ComportamentoEnum;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.MaturidadeEnum;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusEnum;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalGenericoRequisicao {


    private String chipId;

    private String nome;

    @NotBlank(message = "A raça não pode estar em branco")
    private String raca;

    @NotNull(message = "A maturidade e obrigatória")
    private MaturidadeEnum maturidade;

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

    private Set<String> doencas;

    private MaezinhaComFilhotesDTO maezinhaComFilhotesDTO;

    private String condicaoAnimal;

    @NotNull(message = "O status de vida é obrigatório")
    private StatusEnum status;


}
