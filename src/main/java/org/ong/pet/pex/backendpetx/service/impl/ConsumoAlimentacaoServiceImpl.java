// ConsumoAlimentacaoServiceImpl.java
package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.ConsumoAlimentoRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.ConsumoAlimentoResposta;
import org.ong.pet.pex.backendpetx.entities.ConsumoAlimento;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.repositories.ConsumoAlimentoRepository;
import org.ong.pet.pex.backendpetx.service.ConsumoAlimentoService;
import org.ong.pet.pex.backendpetx.service.exceptions.ConsumoAlimentoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumoAlimentacaoServiceImpl implements ConsumoAlimentoService {

    private final ConsumoAlimentoRepository repository;
    private final ConsumoAlimentoRepository consumoAlimentoRepository;

    public ConsumoAlimentacaoServiceImpl(ConsumoAlimentoRepository repository, ConsumoAlimentoRepository consumoAlimentoRepository) {
        this.repository = repository;
        this.consumoAlimentoRepository = consumoAlimentoRepository;
    }

    @Override
    public ConsumoAlimentoResposta cadastrarConsumoAlimento(final ConsumoAlimentoRequisicao dto) {

        ConsumoAlimento dado = consumoAlimentoRepository.findByPorte(dto.porte()).map(entidade -> {
            entidade.setConsumoDiario(dto.consumoDiario());
            return repository.saveAndFlush(entidade);
        }).orElseGet(() -> {
            ConsumoAlimento entidade = new ConsumoAlimento();
            entidade.setPorte(dto.porte());
            entidade.setConsumoDiario(dto.consumoDiario());
            return repository.save(entidade);
        });
        return new ConsumoAlimentoResposta(dado.getId(), dado.getPorte(), dado.getConsumoDiario());
    }

    @Override
    public ConsumoAlimentoResposta buscarPorPorte(final PorteEnum porte) {
        ConsumoAlimento dado = repository.findByPorte(porte).orElseThrow(() -> new ConsumoAlimentoException("Consumo com " + porte + " não encontrado"));
        return new ConsumoAlimentoResposta(dado.getId(), dado.getPorte(), dado.getConsumoDiario());
    }

    @Override
    public List<ConsumoAlimentoResposta> buscarTodos() {
        List<ConsumoAlimento> list = repository.findAll();
        return list.stream().map(dado -> new ConsumoAlimentoResposta(dado.getId(), dado.getPorte(), dado.getConsumoDiario())).toList();
    }


    @Override
    public ConsumoAlimentoResposta atualizar(final ConsumoAlimentoRequisicao dto) {

        var entidade = repository.findByPorte(dto.porte()).orElseThrow(() -> new ConsumoAlimentoException("Consumo com o porte: " + dto.porte() + " não encontrado"));
        entidade.setPorte(dto.porte());
        entidade.setConsumoDiario(dto.consumoDiario());
        entidade = repository.save(entidade);
        return new ConsumoAlimentoResposta(entidade.getId(), entidade.getPorte(), entidade.getConsumoDiario());
    }

    @Override
    public void deletarPorPorte(final PorteEnum porte) {
        var entidade = repository.findByPorte(porte).orElseThrow(() -> new ConsumoAlimentoException("Consumo com " + porte + " não encontrado"));
        repository.delete(entidade);
    }
}