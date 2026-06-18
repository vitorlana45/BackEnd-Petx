package org.ong.pet.pex.backendpetx.entity.revision;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserRevisionListener implements RevisionListener {
  @Override public void newRevision(Object revisionEntity) {
    Revisao rev = (Revisao) revisionEntity;
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    rev.setUsername(auth != null ? auth.getName() : "sistema");
  }
}
