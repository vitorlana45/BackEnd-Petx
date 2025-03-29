package org.ong.pet.pex.backendpetx.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ong.pet.pex.backendpetx.controllers.exceptions.StandardError;
import org.ong.pet.pex.backendpetx.service.exceptions.ValidacaoFiltroParametroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ValidacaoFiltroParametro implements Filter {


    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(ValidacaoFiltroParametro.class);

    // Parâmetros aceitos na busca paginada de aniamis
    private static final Set<String> ACCEPTED_PARAMETERS = Set.of(
            "nome",
            "especie",
            "porte",
            "maturidade",
            "status",
            "doenca",
            "comportamento",
            "origem",
            "sexo",
            "page",
            "size",
            "sort"
    );

    public ValidacaoFiltroParametro(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Chama a função para validar os parâmetros
        try {
            validarParametros(httpRequest, httpResponse);
        } catch (ValidacaoFiltroParametroException ex) {

            StandardError error = new StandardError();

            // verificar esta linha esta dando erro de serialização
            error.setTimestamp(Instant.now());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            error.setError("Requisição Inválida");
            error.setMessage(ex.getMessage());
            error.setPath(httpRequest.getRequestURI());

            // Configura a resposta para retornar o erro em formato JSON
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setHeader("Content-Type", "application/json");
            Map<String, String> time = Map.of("timestamp", Instant.now().toString());

            httpResponse.getWriter().write(converterObjetoParaJson(error));
            return;
        }
        // Continua com a requisição se tudo estiver válido
        chain.doFilter(request, response);
    }

    private void validarParametros(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        // Obtém os parâmetros da URL
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        List<String> invalidParameters = new ArrayList<>();

        // Verifica se algum parâmetro não é aceito
        for (String parameter : parameterMap.keySet()) {
            if (!ACCEPTED_PARAMETERS.contains(parameter)) {
                invalidParameters.add(parameter);
            }
        }

       //TODO: verificar se o erro esta sendo tratado corretamente
        // Se houver parâmetros inválidos, retorna erro
        if (!invalidParameters.isEmpty()) {
            String errorMessage = "Parâmetros inválidos: " + String.join(", ", invalidParameters) +
                                  ". Parâmetros aceitos: " + String.join(", ", ACCEPTED_PARAMETERS);
            throw new ValidacaoFiltroParametroException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    // Função para converter o objeto em JSON
    private String converterObjetoParaJson(StandardError error) {
        try {
            return objectMapper.writeValueAsString(error);
        } catch (IOException e) {
            logger.error("Erro ao converter StandardError para JSON", e);
            return "{}";
        }
    }
}
