package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;

/**
 * ObjectMapper wrapper, singleton
 */
public enum JsonMapper {

    MAPPER;

    /**
     * @return a copy of an ObjectMapper used by the JsonMapper singleton; useful when customized behaviour is needed
     */
    public static ObjectMapper newMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    // WRAPPER METHODS

    public byte[] writeValueAsBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    public String writeValueAsString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    public <T> ObjectReader readerFor(Class<T> clazz) throws IOException {
        return mapper.readerFor(clazz);
    }

    public <T> ObjectReader readerFor(JavaType type) throws IOException {
        return mapper.readerFor(type);
    }

    public <T> ObjectReader readerFor(TypeReference<T> type) throws IOException {
        return mapper.readerFor(type);
    }

    // PRIVATE

    JsonMapper() {
        this.mapper = newMapper();
    }

    private final ObjectMapper mapper;

}
