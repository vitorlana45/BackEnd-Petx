package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "contato_db")
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cep;
    private String cidade;
    private String bairro;
    private String rua;
    private Integer numeroCelular;
}
