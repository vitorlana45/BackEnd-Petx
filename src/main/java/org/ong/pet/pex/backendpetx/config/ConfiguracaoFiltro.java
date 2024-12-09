package org.ong.pet.pex.backendpetx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.ong.pet.pex.backendpetx.infra.ValidacaoFiltroParametro;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
public class ConfiguracaoFiltro {

    @Bean
    public ValidacaoFiltroParametro validacaoFiltroParametro(ObjectMapper objectMapper) {
        return new ValidacaoFiltroParametro(objectMapper);
    }

    @Bean
    public FilterRegistrationBean<ValidacaoFiltroParametro> filtroCustomizado(ValidacaoFiltroParametro filtroParametro) {
        FilterRegistrationBean<ValidacaoFiltroParametro> registrandoBean = new FilterRegistrationBean<>(filtroParametro);
        registrandoBean.addUrlPatterns("/api/animais/filtro/*");
        registrandoBean.setOrder(1);
        return registrandoBean;
    }
}
