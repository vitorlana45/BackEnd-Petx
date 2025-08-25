package org.ong.pet.pex.backendpetx.security.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;

public interface CurrentUser {
  String username();
  Set<String> roles();
  <T> T principalAs(Class<T> type);
}

@Component
class SecurityCurrentUser implements CurrentUser {

  public String username() {
    return SecurityUtils.requireUsername();
  }

  public Set<String> roles() {
    return SecurityUtils.requireAuth()
            .getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(java.util.stream.Collectors.toSet());
  }

  // irei mandar classe ANIMAL e ele ira retornar o principal como ANIMAL, se nao lanca erro
  public <T> T principalAs(Class<T> type) {
    return SecurityUtils.requirePrincipal(type);
  }

}
