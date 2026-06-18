package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "activity_log")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LogAtividade {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ts", nullable = false)
  private OffsetDateTime ts;

  private String usuario;

  @Column(nullable = false)
  private String acao;        // ex.: "CRIADO", "ALTERADO"

  @Column(nullable = false)
  private String entidade;    // ex.: "ANIMAL"

  @Column(name = "entidade_id")
  private Long entidadeId;

  @Column(length = 1000)
  private String mensagem;

  @JdbcTypeCode(SqlTypes.JSON)        // Hibernate 6: mapeia JSONB
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> meta;
}
