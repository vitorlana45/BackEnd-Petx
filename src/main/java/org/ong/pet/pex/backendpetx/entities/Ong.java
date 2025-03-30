package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "ong_tb")
public class Ong extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

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

    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Boletim> boletins;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ong ong = (Ong) o;
        return Objects.equals(id, ong.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
