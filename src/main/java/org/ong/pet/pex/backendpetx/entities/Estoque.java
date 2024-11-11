package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "estoque_tb")
public class Estoque extends EntidadeBase {

    @Column(name = "nome")
    private String racao;
    @Column(name = "quantidade")
    private String quantidade;
    @Column(name = "preco")
    private EspecieEnum especie;
    @Column(name = "porte")
    private PorteEnum porte;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ong")
    private Ong ong;

}
