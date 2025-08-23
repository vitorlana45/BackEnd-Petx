package org.ong.pet.pex.backendpetx.service.cache;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Componente responsável por armazenar em cache as estatísticas do sistema.
 * As estatísticas são carregadas durante a inicialização da aplicação e
 * podem ser acessadas e atualizadas por qualquer parte do sistema.
 */
@Component
public class EstatisticasCache {

    // Cache de estatísticas usando um mapa thread-safe
    private final Map<String, Object> estatisticasCache = new ConcurrentHashMap<>();

    // Chaves para os diferentes tipos de estatísticas
    public static final String TOTAL_ANIMAIS = "totalAnimais";
    public static final String TOTAL_TUTORES = "totalTutores";
    public static final String TOTAL_ADOCOES = "totalAdocoes";
    public static final String ANIMAIS_POR_ESPECIE = "animaisPorEspecie";
    public static final String ADOCOES_POR_MES = "adocoesPorMes";

    /**
     * Adiciona ou atualiza uma estatística no cache.
     *
     * @param chave A chave da estatística.
     * @param valor O valor da estatística.
     */
    public void atualizarEstatistica(String chave, Object valor) {
        estatisticasCache.put(chave, valor);
    }

    /**
     * Obtém uma estatística do cache.
     *
     * @param chave A chave da estatística.
     * @return O valor da estatística, ou null se não existir.
     */
    public Object getEstatistica(String chave) {
        return estatisticasCache.get(chave);
    }

    /**
     * Obtém uma estatística long do cache.
     *
     * @param chave A chave da estatística.
     * @return O valor long da estatística, ou 0 se não existir.
     */
    public long getEstatisticaLong(String chave) {
        Object valor = estatisticasCache.get(chave);
        if (valor instanceof Long) {
            return (Long) valor;
        } else if (valor instanceof Number) {
            return ((Number) valor).longValue();
        }
        return 0L;
    }

    /**
     * Obtém uma estatística Map do cache.
     *
     * @param chave A chave da estatística.
     * @return O mapa da estatística, ou um mapa vazio se não existir.
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getEstatisticaMap(String chave) {
        Object valor = estatisticasCache.get(chave);
        if (valor instanceof Map) {
            return (Map<K, V>) valor;
        }
        return new HashMap<>();
    }

    /**
     * Verifica se uma estatística existe no cache.
     *
     * @param chave A chave da estatística.
     * @return true se a estatística existir, false caso contrário.
     */
    public boolean contemEstatistica(String chave) {
        return estatisticasCache.containsKey(chave);
    }

    /**
     * Limpa todas as estatísticas do cache.
     */
    public void limparCache() {
        estatisticasCache.clear();
    }
}
