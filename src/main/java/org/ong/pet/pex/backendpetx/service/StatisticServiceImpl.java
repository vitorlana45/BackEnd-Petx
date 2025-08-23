package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.service.cache.EstatisticasCache;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final AnimalService animalService;
    private final TutorService tutorService;
    private final BoletimService boletimService;
    private final EstatisticasCache estatisticasCache;

    public StatisticServiceImpl(AnimalService animalService, TutorService tutorService,
                               BoletimService boletimService, EstatisticasCache estatisticasCache) {
        this.animalService = animalService;
        this.tutorService = tutorService;
        this.boletimService = boletimService;
        this.estatisticasCache = estatisticasCache;
    }

    /**
     * Carrega todas as estatísticas do sistema e armazena no cache.
     * Este método deve ser chamado durante a inicialização da aplicação.
     */
    public void carregarTodasEstatisticas() {
        // Carrega estatísticas básicas
        atualizarEstatisticaAnimais();
        atualizarEstatisticaTutores();
        atualizarEstatisticaAdocoes();

        // Aqui você pode adicionar mais carregamentos de estatísticas específicas
        // Por exemplo: distribuição de animais por espécie, adoções por mês, etc.
    }

    /**
     * Atualiza a estatística de animais no cache.
     */
    public void atualizarEstatisticaAnimais() {
        long quantidade = animalService.contarQuantidadeAnimais();
        estatisticasCache.atualizarEstatistica(EstatisticasCache.TOTAL_ANIMAIS, quantidade);
    }

    /**
     * Atualiza a estatística de tutores no cache.
     */
    public void atualizarEstatisticaTutores() {
        long quantidade = tutorService.getTotalTutores();
        estatisticasCache.atualizarEstatistica(EstatisticasCache.TOTAL_TUTORES, quantidade);
    }

    /**
     * Atualiza a estatística de adoções no cache.
     */
    public void atualizarEstatisticaAdocoes() {
        // Implementação futura: usar boletimService para calcular adoções reais
        long quantidade = 20L; // Valor temporário
        estatisticasCache.atualizarEstatistica(EstatisticasCache.TOTAL_ADOCOES, quantidade);
    }

    @Override
    public long getQuantidadeTutores() {
        // Verifica se já existe no cache, caso contrário carrega
        if (!estatisticasCache.contemEstatistica(EstatisticasCache.TOTAL_TUTORES)) {
            atualizarEstatisticaTutores();
        }
        return estatisticasCache.getEstatisticaLong(EstatisticasCache.TOTAL_TUTORES);
    }

    @Override
    public long getQuantidadeAnimais() {
        // Verifica se já existe no cache, caso contrário carrega
        if (!estatisticasCache.contemEstatistica(EstatisticasCache.TOTAL_ANIMAIS)) {
            atualizarEstatisticaAnimais();
        }
        return estatisticasCache.getEstatisticaLong(EstatisticasCache.TOTAL_ANIMAIS);
    }

    @Override
    public long getQuantidadeAdocoes() {
        // Verifica se já existe no cache, caso contrário carrega
        if (!estatisticasCache.contemEstatistica(EstatisticasCache.TOTAL_ADOCOES)) {
            atualizarEstatisticaAdocoes();
        }
        return estatisticasCache.getEstatisticaLong(EstatisticasCache.TOTAL_ADOCOES);
    }
}
