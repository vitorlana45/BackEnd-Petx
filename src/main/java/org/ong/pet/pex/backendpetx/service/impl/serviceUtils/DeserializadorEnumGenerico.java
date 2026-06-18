package org.ong.pet.pex.backendpetx.service.impl.serviceUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.ong.pet.pex.backendpetx.service.exceptions.EnumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;

public class DesserializadorEnumGenerico<T extends Enum<T>> extends JsonDeserializer<T> {
    private static final Logger logger = LoggerFactory.getLogger(DesserializadorEnumGenerico.class);

    private final Class<T> enumClass;

    public DesserializadorEnumGenerico(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T deserialize(JsonParser jsonDTO, DeserializationContext ctxt) throws IOException {
        String valor = jsonDTO.getText();
        String campoErro = jsonDTO.currentName();
        logger.info("iniciando a deserializacao do: ''{}'', com o valor ''{}''", enumClass.getSimpleName(), valor);

        // se for nulo uso a anotação @NotNull
        if (valor == null) {
            return null;
        }

        String normalizadoOValor = valor.trim().toUpperCase();

        logger.info("iniciando a verificacao se o valor ''{}'' é valido", normalizadoOValor);
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(item ->
                        item.name().equals(normalizadoOValor) ||
                        removerAcentos(item.name()).equals(removerAcentos(normalizadoOValor)))
                .findFirst()
                .orElseThrow(() -> new EnumException(
                        "Valor inválido: '" + valor  + "':" + " Valores esperados: " + Arrays.toString(enumClass.getEnumConstants()),
                        HttpStatus.BAD_REQUEST,
                        campoErro
                ));
    }

    // sanitizando a string para remover acentos
    private static String removerAcentos(String input) {
        return input
                .replaceAll("[ÁÀÂÃÄáàâãä]", "A")
                .replaceAll("[ÉÈÊËéèêë]", "E")
                .replaceAll("[ÍÌÎÏíìîï]", "I")
                .replaceAll("[ÓÒÔÕÖóòôõö]", "O")
                .replaceAll("[ÚÙÛÜúùûü]", "U")
                .replaceAll("[Çç]", "C");
    }
}
