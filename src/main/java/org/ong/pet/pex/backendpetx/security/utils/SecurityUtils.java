package org.ong.pet.pex.backendpetx.security.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
  private SecurityUtils() {}



  /** Retorna a Authentication atual ou lança IllegalStateException se não houver login válido. */
  public static Authentication requireAuth() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
      throw new IllegalStateException("Usuário não autenticado");
    }
    return auth;
  }

  /** Retorna o principal tipado; lança se o principal não for do tipo esperado. */
  public static <T> T requirePrincipal(Class<T> type) {
    Object p = requireAuth().getPrincipal();
    if (!type.isInstance(p)) {
      throw new IllegalStateException("Principal não é do tipo esperado: " + type.getName());
    }
    return type.cast(p);
  }

  /** Retorna o username do usuário autenticado (Authentication.getName()). */
  public static String requireUsername() {
    return requireAuth().getName();
  }


}
