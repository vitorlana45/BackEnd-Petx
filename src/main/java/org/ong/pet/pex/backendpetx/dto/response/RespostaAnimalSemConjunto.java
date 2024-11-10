package org.ong.pet.pex.backendpetx.dto.response;

public record RespostaAnimalSemConjunto(
        Long id,
        String nome,
        Integer idade,
        String raca,
        String sexo,
        String origem, // Novo campo adicionado para origem
        String porte,
        String comportamento,
        String especie
) {
}
