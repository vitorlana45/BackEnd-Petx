package org.ong.pet.pex.backendpetx.controller.animal;

import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.entity.media.MediaFile;
import org.ong.pet.pex.backendpetx.dto.MediaItemDTO;
import org.ong.pet.pex.backendpetx.entity.media.MediaTargetType;
import org.ong.pet.pex.backendpetx.entity.media.MediaUsage;
import org.ong.pet.pex.backendpetx.repository.media.MediaLinkRepository;
import org.ong.pet.pex.backendpetx.service.impl.MediaService;
import org.ong.pet.pex.backendpetx.service.mediaService.MediaStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/animais/{animalId}/midias")
@RequiredArgsConstructor
public class AnimalMediaController {
    private final MediaService mediaService;
    private final MediaStorageService storage;
    private final MediaLinkRepository linkRepo;

    @PostMapping
    public String upload(@PathVariable Long animalId, @RequestParam("file") MultipartFile file, Model model) {
        mediaService.uploadAndLink(file, MediaTargetType.ANIMAL, animalId, MediaUsage.GALERIA, 0);
        // devolve o fragmento já atualizado (ótimo para HTMX)
        var itens = linkRepo.findByTarget(MediaTargetType.ANIMAL, animalId).stream().map(l -> {
            MediaFile f = l.getMediaFile();
            return new MediaItemDTO(f.getId(),storage.presignGetUrl(f.getObjectKey()), f.getOriginalFilename(), l.getUsage(), l.getSortOrder());
        }).toList();
        model.addAttribute("itens", itens);
        return "animais/galeria :: grid";
    }
}