package org.ong.pet.pex.backendpetx.dto.projection;

import org.ong.pet.pex.backendpetx.enums.*;

public record AnimalListDto(
        Long id,
        String chipId,
        String nome,
        String raca,
        MaturidadeEnum maturidade,
        SexoEnum sexo,
        OrigemAnimalEnum origem,
        PorteEnum porte,
        String comportamento,
        EspecieEnum especie,
        SaudeEnum saude,
        AdocaoEnum adotado,
        String imagemPrincipalPerfil
) {}
