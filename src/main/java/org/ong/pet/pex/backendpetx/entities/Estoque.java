package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "estoque_tb")
public class Estoque extends EntidadeBase {


    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL)
    private List<Produto> produto;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ong")
    private Ong ong;

}
