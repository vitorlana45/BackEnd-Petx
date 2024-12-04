package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "obito_tb")
public class Obito extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "data_obito")
    private Date dataObito;

    @Column(name = "motivo_obito")
    private String motivoObito;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Animal animal;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Obito obito = (Obito) o;
        return Objects.equals(id, obito.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
