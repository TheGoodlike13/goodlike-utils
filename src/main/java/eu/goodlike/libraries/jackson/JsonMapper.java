package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;

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
        return new ObjectMapper()
                .registerModule(new ParanamerModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    // WRAPPER METHODS

    public byte[] writeValueAsBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    public String writeValueAsString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    public <T> ObjectReader readerFor(Class<T> clazz) {
        return mapper.readerFor(clazz);
    }

    public <T> ObjectReader readerFor(JavaType type) {
        return mapper.readerFor(type);
    }

    public <T> ObjectReader readerFor(TypeReference<T> type) {
        return mapper.readerFor(type);
    }

    // PRIVATE

    JsonMapper() {
        this.mapper = newMapper();
    }

    private final ObjectMapper mapper;

}
