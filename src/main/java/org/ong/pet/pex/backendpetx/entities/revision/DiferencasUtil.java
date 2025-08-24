// Diffs.java
package org.ong.pet.pex.backendpetx.entities.revision;

import java.lang.reflect.Field;
import java.util.*;

public final class DiferencaCampo {
  private DiferencaCampo(){}

  public static <T> List<FieldDiff> diff(T a, T b, Collection<String> ignore) {
    if (a == null || b == null) return List.of();
    List<FieldDiff> out = new ArrayList<>();
    Class<?> c = a.getClass();
    for (Field f : c.getDeclaredFields()) {
      String name = f.getName();
      if (ignore != null && ignore.contains(name)) continue;
      f.setAccessible(true);
      try {
        Object va = f.get(a);
        Object vb = f.get(b);
        if (!Objects.equals(va, vb)) {
          out.add(new FieldDiff(name, toStr(va), toStr(vb)));
        }
      } catch (IllegalAccessException ignored) {}
    }
    return out;
  }

  private static String toStr(Object o){ return o == null ? "—" : String.valueOf(o); }
}
