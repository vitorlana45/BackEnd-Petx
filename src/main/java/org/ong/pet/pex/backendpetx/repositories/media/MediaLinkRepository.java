package org.ong.pet.pex.backendpetx.repositories.media;

import org.ong.pet.pex.backendpetx.entities.media.MediaLink;
import org.ong.pet.pex.backendpetx.entities.media.MediaTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaLinkRepository extends JpaRepository<MediaLink, Long> {
    @Query("select ml from MediaLink ml join fetch ml.mediaFile " +
            "where ml.targetType = :type and ml.targetId = :id " +
            "order by ml.sortOrder asc, ml.id asc")
    List<MediaLink> findByTarget(@Param("type") MediaTargetType type, @Param("id") Long id);
}
