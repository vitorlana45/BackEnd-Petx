package org.ong.pet.pex.backendpetx.config;

import org.ong.pet.pex.backendpetx.service.StatisticServiceImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuração para inicializar estatísticas na inicialização da aplicação.
 * Esta classe carrega todas as estatísticas no cache assim que a aplicação é iniciada,
 * tornando-as disponíveis imediatamente para todas as partes do sistema.
 */
@Configuration
public class EstatisticasInitializr {

    private static final Logger logger = LoggerFactory.getLogger(EstatisticasInitializr.class);

//    private final StatisticServiceImpl statisticService;

//    public EstatisticasInitializr(StatisticServiceImpl statisticService) {
//        this.statisticService = statisticService;
//    }

//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        logger.info("Iniciando carregamento das estatísticas do sistema...");
//
//        try {
//            // Carrega todas as estatísticas no cache
//            statisticService.recarregarEstatisticas();
//            logger.info("Estatísticas do sistema carregadas com sucesso!");
//        } catch (Exception e) {
//            logger.error("Erro ao carregar estatísticas do sistema: {}", e.getMessage(), e);
//        }
//    }
}
