package org.ong.pet.pex.backendpetx.entities;

import lombok.*;

@Getter
public enum Animal {
    cachorro("cachorro"),
    gato ("gato");
    private final String animal;

    Animal(String animal) {
        this.animal = animal;
    }

}
