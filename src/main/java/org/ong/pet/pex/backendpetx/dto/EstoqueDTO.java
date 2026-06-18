package org.ong.pet.pex.backendpetx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EstoqueDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String categoriaPrincipalNome;
    private String categoriaPrincipalDescricao;
}
