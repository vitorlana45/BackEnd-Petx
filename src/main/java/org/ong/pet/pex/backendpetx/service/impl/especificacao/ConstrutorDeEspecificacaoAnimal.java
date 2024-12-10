package org.ong.pet.pex.backendpetx.service.impl.especificacao;

import jakarta.persistence.criteria.Join;
import lombok.Getter;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class ConstrutorDeEspecificacaoAnimal extends ConstrutorDeEspecificacaoGenerica<Animal> {

    public ConstrutorDeEspecificacaoAnimal adicionarFiltroPorDoenca(String nomeAtributo, String doenca ) {

        if (StringUtils.hasText(doenca)) {
            Specification<Animal> especificacao =  getEspecificacao().and((raiz, consulta, criteriosConstrutor) -> {
                String valorNormalizado = doenca.toLowerCase().trim();

                // usando o Join para acessar a lista de doencas do animal
                Join<Object, String> join = raiz.join(nomeAtributo);
                // o predicado é criado para verificar se a lista de doencas contém a doença
                return criteriosConstrutor.like(criteriosConstrutor.lower(join), "%" + valorNormalizado + "%");

            });
            setEspecificacao(especificacao);
        }
        return this;
    }
}