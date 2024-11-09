package org.ong.pet.pex.backendpetx.dto.request;

import jakarta.validation.constraints.*;
import org.ong.pet.pex.backendpetx.enums.*;

import java.util.Set;

public record AnimalDTO(

        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @NotNull(message = "A idade é obrigatória")
        @Min(value = 0, message = "A idade não pode ser negativa")
        Integer idade,

        @NotBlank(message = "A raça não pode estar em branco")
        String raca,

        @NotNull(message = "O sexo é obrigatório")
        SexoEnum sexoEnum,

        @NotNull(message = "A origem é obrigatória")
        OrigemAnimalEnum origemEnum, // Novo campo adicionado para origem

        @NotNull(message = "O porte é obrigatório")
        PorteEnum porteEnum,

        @NotNull(message = "O comportamento é obrigatório")
        ComportamentoEnum comportamentoEnum,

        @NotNull(message = "A espécie é obrigatória")
        EspecieEnum especieEnum,

        Set<AnimalConjuntoDTO> animalConjunto

) {}
