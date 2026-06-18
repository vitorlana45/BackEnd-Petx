package org.ong.pet.pex.backendpetx.entity;

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