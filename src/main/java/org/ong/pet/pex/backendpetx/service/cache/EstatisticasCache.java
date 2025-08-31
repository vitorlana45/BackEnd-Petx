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


    public void remover(String chave) {
        estatisticasCache.remove(chave);
    }

    public void limparCache() {
        estatisticasCache.clear();
    }
}
