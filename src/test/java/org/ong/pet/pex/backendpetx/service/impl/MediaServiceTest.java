package org.ong.pet.pex.backendpetx.service.impl;

import org.junit.jupiter.api.Test;
import org.ong.pet.pex.backendpetx.entity.media.MediaFile;
import org.ong.pet.pex.backendpetx.entity.media.MediaLink;
import org.ong.pet.pex.backendpetx.entity.media.MediaTargetType;
import org.ong.pet.pex.backendpetx.entity.media.MediaUsage;
import org.ong.pet.pex.backendpetx.repository.media.MediaFileRepository;
import org.ong.pet.pex.backendpetx.repository.media.MediaLinkRepository;
import org.ong.pet.pex.backendpetx.service.mediaService.MediaStorageService;
import org.ong.pet.pex.backendpetx.service.mediaService.ObjectKeyBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Garante o comportamento em lote da foto de perfil (evita regressão do N+1
 * e do enriquecimento de listagens).
 */
class MediaServiceTest {

    private final MediaStorageService storage = mock(MediaStorageService.class);
    private final MediaFileRepository fileRepo = mock(MediaFileRepository.class);
    private final MediaLinkRepository linkRepo = mock(MediaLinkRepository.class);
    private final ObjectKeyBuilder keys = mock(ObjectKeyBuilder.class);

    private final MediaService service = new MediaService(storage, fileRepo, linkRepo, keys);

    @Test
    void getProfilePresignedUrls_idsVazios_naoConsultaBanco() {
        Map<Long, String> result = service.getProfilePresignedUrls(MediaTargetType.ANIMAL, List.of());

        assertThat(result).isEmpty();
        verifyNoInteractions(linkRepo, storage);
    }

    @Test
    void getProfilePresignedUrls_mapeiaTargetIdParaUrl_emUmaConsulta() {
        MediaLink l1 = link(10L, "animals/10/foto.jpg");
        MediaLink l2 = link(20L, "animals/20/foto.jpg");
        when(linkRepo.findByTargetTypeAndUsageForIds(MediaTargetType.ANIMAL, List.of(10L, 20L), MediaUsage.PERFIL))
                .thenReturn(List.of(l1, l2));
        when(storage.presignGetUrl("animals/10/foto.jpg")).thenReturn("url-10");
        when(storage.presignGetUrl("animals/20/foto.jpg")).thenReturn("url-20");

        Map<Long, String> result = service.getProfilePresignedUrls(MediaTargetType.ANIMAL, List.of(10L, 20L));

        assertThat(result).containsOnly(Map.entry(10L, "url-10"), Map.entry(20L, "url-20"));
        // uma única consulta em lote (sem N+1)
        verify(linkRepo, times(1)).findByTargetTypeAndUsageForIds(any(), anyList(), eq(MediaUsage.PERFIL));
    }

    private MediaLink link(Long targetId, String objectKey) {
        MediaFile file = mock(MediaFile.class);
        when(file.getObjectKey()).thenReturn(objectKey);
        MediaLink link = mock(MediaLink.class);
        when(link.getTargetId()).thenReturn(targetId);
        when(link.getMediaFile()).thenReturn(file);
        return link;
    }
}
