package org.ong.pet.pex.backendpetx.service.impl;

import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.entities.media.*;
import org.ong.pet.pex.backendpetx.repositories.media.MediaFileRepository;
import org.ong.pet.pex.backendpetx.repositories.media.MediaLinkRepository;
import org.ong.pet.pex.backendpetx.service.mediaService.MediaStorageService;
import org.ong.pet.pex.backendpetx.service.mediaService.ObjectKeyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
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
    public void uploadAndLink(MultipartFile file,
                              MediaTargetType targetType,
                              Long targetId,
                              MediaUsage usage,   // << pode vir PERFIL, mas trataremos como GALERIA na criação
                              Integer sortOrder) {
        byte[] bytes;
        try { bytes = file.getBytes(); } catch (IOException e) { throw new UncheckedIOException(e); }
        String checksum = org.apache.commons.codec.digest.DigestUtils.sha256Hex(bytes);

        // 1) Dedup de arquivo por checksum
        MediaFile mf = fileRepo.findByChecksum(checksum).orElseGet(() -> {
            String ext  = org.apache.commons.io.FilenameUtils.getExtension(file.getOriginalFilename());
            String uuid = java.util.UUID.randomUUID().toString();
            String key  = switch (targetType) {
                case ANIMAL   -> keys.animalOriginal(targetId, uuid, ext, java.time.LocalDate.now(), usage);
                case CONSULTA -> keys.consultaOriginal(targetId, uuid, ext, java.time.LocalDate.now(), usage);
            };

            storage.putObject(key, file.getContentType(), new java.io.ByteArrayInputStream(bytes), file.getSize());

            MediaFile n = new MediaFile();
            n.setObjectKey(key);
            n.setContentType(java.util.Objects.requireNonNullElse(file.getContentType(), "application/octet-stream"));
            n.setSizeBytes(file.getSize());
            n.setChecksum(checksum);
            n.setOriginalFilename(file.getOriginalFilename());
            return fileRepo.save(n);
        });

        // 2) Sempre cria/garante o link como GALERIA (idempotente)
        MediaLink link = linkRepo
                .findByMediaFileIdAndTargetTypeAndTargetIdAndUsage(mf.getId(), targetType.name(), targetId, MediaUsage.GALERIA)
                .orElseGet(() -> {
                    MediaLink l = new MediaLink();
                    l.setMediaFile(mf);
                    l.setTargetType(targetType.name());
                    l.setTargetId(targetId);
                    l.setUsage(MediaUsage.GALERIA);
                    l.setSortOrder(sortOrder == null ? 0 : sortOrder);
                    return linkRepo.save(l);
                });

        // 3) Se o caller pediu PERFIL, promove esse link para PERFIL (e rebaixa os outros)
        if (usage == MediaUsage.PERFIL) {
            markAsProfile(mf.getId(), targetType, targetId);
        }

    }

    @Transactional(readOnly = true)
    public List<MediaFile> list(MediaTargetType type, Long id) {
        return linkRepo.findByTarget(type, id).stream().map(MediaLink::getMediaFile).toList();
    }


    @Transactional
    public void markAsProfile(Long mediaFileId, MediaTargetType targetType, Long targetId) {
        // 1) Já existe link desse arquivo para o target? Se não existir, cria como GALERIA e depois promove
        MediaLink link = linkRepo
                .findByMediaFileIdAndTargetTypeAndTargetId(mediaFileId, targetType.name(), targetId)
                .orElseGet(() -> {
                    MediaLink l = new MediaLink();
                    MediaFile mf = fileRepo.findById(mediaFileId).orElseThrow();
                    l.setMediaFile(mf);
                    l.setTargetType(targetType.name());
                    l.setTargetId(targetId);
                    l.setUsage(MediaUsage.GALERIA);
                    l.setSortOrder(0);
                    return linkRepo.save(l);
                });

        // 2) Rebaixa outros PERFIL -> GALERIA e promove o atual
        java.util.List<MediaLink> links = linkRepo.findByTarget(targetType, targetId);
        for (MediaLink l : links) {
            if (l.getMediaFile().getId().equals(mediaFileId)) {
                l.setUsage(MediaUsage.PERFIL);
            } else if (l.getUsage() == MediaUsage.PERFIL) {
                l.setUsage(MediaUsage.GALERIA);
            }
        }
        linkRepo.saveAll(links);
    }

    @Transactional
    public void unsetProfile(MediaTargetType targetType, Long targetId) {
        java.util.List<MediaLink> links = linkRepo.findByTarget(targetType, targetId);
        for (MediaLink l : links) {
            if (l.getUsage() == MediaUsage.PERFIL) {
                l.setUsage(MediaUsage.GALERIA);
            }
        }
        linkRepo.saveAll(links);
    }

    @Transactional(readOnly = true)
    public String getProfilePresignedUrl(MediaTargetType targetType, Long targetId) {
        return linkRepo.findFirstByTargetTypeAndTargetIdAndUsage(targetType.name(), targetId, MediaUsage.PERFIL)
                .map(MediaLink::getMediaFile)
                .map(MediaFile::getObjectKey)
                .map(storage::presignGetUrl)
                .orElse(null);
    }
    // MediaService.listWithUrls(...)
    @Transactional(readOnly = true)
    public List<MediaItemDTO> listWithUrls(MediaTargetType type, Long id) {
        return linkRepo.findByTarget(type, id).stream()
                .map(l -> new MediaItemDTO(
                        l.getMediaFile().getId(),                                     // <-- id do MediaFile
                        storage.presignGetUrl(l.getMediaFile().getObjectKey()),
                        Objects.toString(l.getMediaFile().getOriginalFilename(), "arquivo"),
                        l.getUsage(),
                        l.getSortOrder()
                ))
                .toList();
    }

    /**
     * Exclui uma foto específica (remove link)
     */
    @Transactional
    public void deleteMedia(Long mediaFileId) {
        // Remove o link específico (mantém o arquivo para outros possíveis usos)
        MediaFile mediaFile = fileRepo.findById(mediaFileId).orElse(null);
        if (mediaFile != null) {
            // Remove todos os links para este arquivo usando query customizada
            linkRepo.deleteAll(linkRepo.findAll().stream()
                .filter(l -> l.getMediaFile().getId().equals(mediaFileId))
                .toList());
        }
    }

}
