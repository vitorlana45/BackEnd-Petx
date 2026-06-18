package org.ong.pet.pex.backendpetx.repositories.specifcs;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.enums.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;

import static org.springframework.util.StringUtils.hasText;

public class AnimalSpecs {
    public static Specification<Animal> filtro(
            String nome, String raca,
            EspecieEnum especie, PorteEnum porte,
            SaudeEnum saude, String comportamento,
            MaturidadeEnum maturidade, OrigemAnimalEnum origem,
            SexoEnum sexo, AdocaoEnum adotado
    ) {
        return (root, query, cb) -> {
            var ps = new ArrayList<Predicate>();

            // @Where já filtra arquivado=false, mas não faz mal reforçar:
            ps.add(cb.isFalse(root.get("arquivado")));

            if (nome != null && !nome.isBlank())
                ps.add(cb.like(cb.lower(root.get("nome")), (nome.toLowerCase() + "%")));
            if (raca != null && !raca.isBlank())
                ps.add(cb.like(cb.lower(root.get("raca")), "%" + raca.toLowerCase() + "%"));
            if (comportamento != null && !comportamento.isBlank())
                ps.add(cb.equal(root.get("comportamento"), comportamento));

            if (especie != null)     ps.add(cb.equal(root.get("especieEnum"), especie));
            if (porte != null)       ps.add(cb.equal(root.get("porteEnum"), porte));
            if (saude != null)       ps.add(cb.equal(root.get("saudeEnum"), saude));
            if (maturidade != null)  ps.add(cb.equal(root.get("maturidadeEnum"), maturidade));
            if (origem != null)      ps.add(cb.equal(root.get("origemEnum"), origem));
            if (sexo != null)        ps.add(cb.equal(root.get("sexoEnum"), sexo));
            if (adotado != null)     ps.add(cb.equal(root.get("adotado"), adotado));

            return cb.and(ps.toArray(Predicate[]::new));
        };
    }
}
