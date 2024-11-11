package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.entities.Estoque;

import java.util.List;

public interface EstoqueService {

    Estoque save(Estoque estoque);
    Estoque delete(Long id);
    List<Estoque> GetAll();
    Estoque update(Long id, Estoque estoque);
}
