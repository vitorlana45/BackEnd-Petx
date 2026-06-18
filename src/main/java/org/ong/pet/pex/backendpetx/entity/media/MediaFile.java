package org.ong.pet.pex.backendpetx.entity.media;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "media_file")
public class MediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ex.: petx/animals/{animalId}/2025/08/31/{uuid}.jpg
    @Column(nullable = false, unique = true, length = 512)
    private String objectKey;

    @Column(nullable = false, length = 128)
    private String contentType;

    @Column(nullable = false)
    private Long sizeBytes;

    @Column(length = 64) // SHA-256/MD5 para dedupe
    private String checksum;

    @Column(length = 160)
    private String originalFilename;

    @Column(nullable = false)
    private Instant uploadedAt = Instant.now();

    // flags/metadados
    private Integer width;
    private Integer height;
    private Boolean hasThumbSmall = false;
    private Boolean hasThumbLarge = false;

    // soft delete opcional
    private Boolean archived = false;
}
