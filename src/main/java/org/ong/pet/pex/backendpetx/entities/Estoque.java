package org.ong.pet.pex.backendpetx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private List<Produto> produto = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    private Ong ong;

}