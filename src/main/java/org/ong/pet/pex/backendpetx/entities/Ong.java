package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "ong_tb")
public class Ong extends EntidadeBase {

    @OneToMany(mappedBy = "ong", fetch = FetchType.EAGER)
    private Set<Animal> animais;

    @OneToMany(mappedBy = "ong", fetch = FetchType.LAZY)
    private Set<Tutor> tutors;

    @OneToMany(mappedBy = "ong", fetch = FetchType.LAZY)
    private Set<Usuario> usuario;

    @OneToMany(mappedBy = "ong", fetch = FetchType.LAZY)
    private List<Despesa> despesas;

    @OneToOne(mappedBy = "ong")
    private Estoque estoque;
}
