package org.ong.pet.pex.backendpetx.entities.media;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "media_link",
       uniqueConstraints = @UniqueConstraint(columnNames = {"media_file_id","target_type","target_id","usage"}))
public class MediaLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MediaFile mediaFile;

    // Polimórfico simples por "type + id"
    @Column(nullable = false, length = 64)
    private String targetType; // "ANIMAL", "CONSULTA"

    @Column(nullable = false)
    private Long targetId;     // id da entidade alvo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private MediaUsage usage;  // GALERIA, PERFIL, EXAME, LAUDO...

    private Integer sortOrder = 0;

    private Instant createdAt = Instant.now();
}
