package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "db_endereco")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cep;
    private String cidade;
    private String bairro;
    private String rua;
}
