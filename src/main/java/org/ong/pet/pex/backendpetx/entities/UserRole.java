package org.ong.pet.pex.backendpetx.entities;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    COLABORADOR("COLABORADOR");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}