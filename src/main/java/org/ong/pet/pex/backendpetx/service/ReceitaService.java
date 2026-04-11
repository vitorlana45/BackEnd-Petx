package org.ong.pet.pex.backendpetx.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ong.pet.pex.backendpetx.entities.Receita;
import org.ong.pet.pex.backendpetx.repositories.ReceitaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceitaService {

    private final ReceitaRepository receitaRepository;

    @Transactional
    public Receita criarReceita(Receita receita) {
        log.info("Criando nova receita: {}", receita.getDescricao());
        return receitaRepository.save(receita);
    }

    @Transactional
    public Receita atualizarReceita(Long id, Receita dadosAtualizados) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada com id: " + id));

        receita.setDescricao(dadosAtualizados.getDescricao());
        receita.setValor(dadosAtualizados.getValor());
        receita.setCategoria(dadosAtualizados.getCategoria());
        receita.setOrigem(dadosAtualizados.getOrigem());
        receita.setDataRecebimento(dadosAtualizados.getDataRecebimento());
        receita.setFormaPagamento(dadosAtualizados.getFormaPagamento());
        receita.setObservacoes(dadosAtualizados.getObservacoes());

        return receitaRepository.save(receita);
    }

    public Page<Receita> listarReceitas(String descricao, BigDecimal valor, LocalDate dataRecebimento,
                                        String categoria, String formaPagamento, Pageable pageable) {
        return receitaRepository.findAllReceitas(descricao, valor, dataRecebimento, categoria, formaPagamento, pageable);
    }

    public Optional<Receita> buscarPorId(Long id) {
        return receitaRepository.findById(id);
    }

    @Transactional
    public void deletarReceita(Long id) {
        receitaRepository.deleteById(id);
    }
}
