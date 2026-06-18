package org.ong.pet.pex.backendpetx.dto;

import org.ong.pet.pex.backendpetx.entity.media.MediaUsage;

public record MediaItemDTO(Long id, String url, String name, MediaUsage usage, Integer order) {}
