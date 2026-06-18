// RevisionRow.java
package org.ong.pet.pex.backendpetx.dto;

import org.hibernate.envers.RevisionType;
import java.time.Instant;

public record RevisaoLinhaDTO(
    Long entityId,
    String entityDisplay,  // nome do animal, por ex.
    long revision,
    Instant when,
    String username,
    RevisionType type
) {}