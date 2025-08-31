package org.ong.pet.pex.backendpetx.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.ong.pet.pex.backendpetx.entities.media.MediaFile;
import org.ong.pet.pex.backendpetx.entities.media.MediaLink;
import org.ong.pet.pex.backendpetx.entities.media.MediaTargetType;
import org.ong.pet.pex.backendpetx.entities.media.MediaUsage;
import org.ong.pet.pex.backendpetx.repositories.media.MediaFileRepository;
import org.ong.pet.pex.backendpetx.repositories.media.MediaLinkRepository;
import org.ong.pet.pex.backendpetx.service.mediaService.MediaStorageService;
import org.ong.pet.pex.backendpetx.service.mediaService.ObjectKeyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaStorageService storage;
    private final MediaFileRepository fileRepo;
    private final MediaLinkRepository linkRepo;
    private final ObjectKeyBuilder keys;

    @Transactional
    public MediaFile uploadAndLink(MultipartFile file,
                                   MediaTargetType targetType, // ANIMAL ou CONSULTA
                                   Long targetId,
                                   MediaUsage usage,           // RESGATE/CONSULTA/etc.
                                   Integer sortOrder) {
        byte[] bytes;
        try { bytes = file.getBytes(); } catch (IOException e) { throw new UncheckedIOException(e); }
        String checksum = DigestUtils.sha256Hex(bytes);

        MediaFile mf = fileRepo.findByChecksum(checksum).orElseGet(() -> {
            String ext  = org.apache.commons.io.FilenameUtils.getExtension(file.getOriginalFilename());
            String uuid = java.util.UUID.randomUUID().toString();
            String key  = switch (targetType) {
                case ANIMAL   -> keys.animalOriginal(targetId, uuid, ext, LocalDate.now(), usage);
                case CONSULTA -> keys.consultaOriginal(targetId, uuid, ext, LocalDate.now(), usage);
            };

            storage.putObject(key, file.getContentType(), new ByteArrayInputStream(bytes), file.getSize());

            MediaFile n = new MediaFile();
            n.setObjectKey(key);
            n.setContentType(Objects.requireNonNullElse(file.getContentType(), "application/octet-stream"));
            n.setSizeBytes(file.getSize());
            n.setChecksum(checksum);
            n.setOriginalFilename(file.getOriginalFilename());
            return fileRepo.save(n);
        });

        MediaLink link = new MediaLink();
        link.setMediaFile(mf);
        link.setTargetType(targetType.name()); // use enum no entity
        link.setTargetId(targetId);
        link.setUsage(usage == null ? MediaUsage.GALERIA : usage);
        link.setSortOrder(sortOrder == null ? 0 : sortOrder);
        linkRepo.save(link);

        return mf;
    }

    @Transactional(readOnly = true)
    public List<MediaFile> list(MediaTargetType type, Long id) {
        return linkRepo.findByTarget(type, id).stream().map(MediaLink::getMediaFile).toList();
    }
}

