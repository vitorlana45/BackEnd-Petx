package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tutor extends Usuario {

    private Integer idade;
    private Integer telefone;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Animal> animal;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Contato contato;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tutor tutor = (Tutor) o;
        return Objects.equals(getId(), tutor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}