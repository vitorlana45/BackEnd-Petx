package org.ong.pet.pex.backendpetx.repositories.media;

import org.ong.pet.pex.backendpetx.entities.media.MediaLink;
import org.ong.pet.pex.backendpetx.entities.media.MediaTargetType;
import org.ong.pet.pex.backendpetx.entities.media.MediaUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MediaLinkRepository extends JpaRepository<MediaLink, Long> {
    @Query("select l from MediaLink l where l.targetType = :#{#type.name()} and l.targetId = :id")
    List<MediaLink> findByTarget(MediaTargetType type, Long id);

    Optional<MediaLink> findFirstByTargetTypeAndTargetIdAndUsage(String targetType, Long targetId, MediaUsage usage);

    Optional<MediaLink> findByMediaFileIdAndTargetTypeAndTargetId(Long mediaFileId, String targetType, Long targetId);

    Optional<MediaLink> findByMediaFileIdAndTargetTypeAndTargetIdAndUsage(Long mediaFileId, String targetType, Long targetId, MediaUsage usage);

    Optional<MediaLink> findByTargetTypeAndTargetIdInAndUsage (String targetType, Collection<Long> targetId, MediaUsage usage);

}
