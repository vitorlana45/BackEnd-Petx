package org.ong.pet.pex.backendpetx.controller.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.MaturidadeEnum;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.enums.SaudeEnum;

import java.time.LocalDateTime;

/**
 * DTO para o cadastro rápido de um animal resgatado.
 * Combina informações básicas do animal e do resgate em um único formulário.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResgateRapidoDTO {

    // Informações do Resgate
    private String tipoResgate;
    private LocalDateTime dataResgate;
    private OrigemAnimalEnum origem;
    private String localResgate;
    private String cidadeResgate;
    private String observacoes;
    
    // Informações Básicas do Animal
    private String nome;
    private EspecieEnum especieEnum;
    private MaturidadeEnum idadeAproximada;
    private SexoEnum sexo;
    private String cor;
    private PorteEnum porte;
    private SaudeEnum saudeEnum;
}
