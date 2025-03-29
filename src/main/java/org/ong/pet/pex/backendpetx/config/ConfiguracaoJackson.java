package org.ong.pet.pex.backendpetx.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.service.impl.serviceUtils.DesserializadorEnumGenerico;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoJackson {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Registra suporte para tipos de data do Java 8
        mapper.registerModule(new JavaTimeModule());

        // Configura a forma de serialização/deserialização de datas
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Configuração genérica para enums
        SimpleModule enumModule = new SimpleModule();
        registerEnumDeserializer(enumModule, CategoriaDespesaEnum.class);
        registerEnumDeserializer(enumModule, FormaPagamentoEnum.class);
        registerEnumDeserializer(enumModule, EspecieEnum.class);
        registerEnumDeserializer(enumModule, MaturidadeEnum.class);
        registerEnumDeserializer(enumModule, OrigemAnimalEnum.class);
        registerEnumDeserializer(enumModule, SexoEnum.class);
        registerEnumDeserializer(enumModule, PorteEnum.class);
        registerEnumDeserializer(enumModule, StatusDespesaEnum.class);
        registerEnumDeserializer(enumModule, StatusEnum.class);
        registerEnumDeserializer(enumModule, TipoProduto.class);
        registerEnumDeserializer(enumModule, UnidadeDeMedidaEnum.class);
        registerEnumDeserializer(enumModule, Destino.class);

        mapper.registerModule(enumModule);

        return mapper;
    }

    private <T extends Enum<T>> void registerEnumDeserializer(SimpleModule module, Class<T> enumClass) {
        module.addDeserializer(enumClass, new DesserializadorEnumGenerico<>(enumClass));
    }
}
