package org.ong.pet.pex.backendpetx.service;

public interface StatisticService {

    /**
     * Retorna a quantidade de tutores cadastrados.
     *
     * @return A quantidade de tutores.
     */
    long getQuantidadeTutores();

    /**
     * Retorna a quantidade de animais cadastrados.
     *
     * @return A quantidade de animais.
     */
    long getQuantidadeAnimais();

    /**
     * Retorna a quantidade de adoções realizadas.
     *
     * @return A quantidade de adoções.
     */
    long getQuantidadeAdocoes();


    /**
     * Retorna a quantidade de boletins cadastrados.
     *
     * @return A quantidade de boletins.
     */
    long getTotalBoletins();

}
