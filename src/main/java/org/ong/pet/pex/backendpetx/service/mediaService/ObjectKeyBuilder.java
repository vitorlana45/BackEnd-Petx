package org.ong.pet.pex.backendpetx.service.mediaService;

import org.ong.pet.pex.backendpetx.entities.media.MediaUsage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class ObjectKeyBuilder {
    @Value("${app.env:dev}") private String env;

    public String animalOriginal(Long animalId, String uuid, String ext, LocalDate d, MediaUsage usage) {
        String folder = usage == null ? "galeria" : usage.name().toLowerCase();
        return String.format(
                "petx/%s/animal/%d/%s/%04d/%02d/%02d/%s.%s",
                env, animalId, folder, d.getYear(), d.getMonthValue(), d.getDayOfMonth(), uuid, ext
        );
    }

    public String consultaOriginal(Long consultaId, String uuid, String ext, LocalDate d, MediaUsage usage) {
        String folder = usage == null ? "galeria" : usage.name().toLowerCase();
        return String.format(
                "petx/%s/consulta/%d/%s/%04d/%02d/%02d/%s.%s",
                env, consultaId, folder, d.getYear(), d.getMonthValue(), d.getDayOfMonth(), uuid, ext
        );
    }
}
