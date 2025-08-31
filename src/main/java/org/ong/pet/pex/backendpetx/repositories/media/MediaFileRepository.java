package org.ong.pet.pex.backendpetx.repositories.media;

import org.ong.pet.pex.backendpetx.entities.media.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    Optional<MediaFile> findByObjectKey(String objectKey);
    Optional<MediaFile> findByChecksum(String checksum);
}