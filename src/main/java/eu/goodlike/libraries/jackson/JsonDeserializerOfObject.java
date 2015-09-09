package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import eu.goodlike.functional.IOFunction;

import java.io.IOException;

/**
 * <pre>
 * De-serializes JSON using a provided ObjectReader function; this function takes form
 *      reader -> reader.readValue(o)
 * where o is some type of JSON container, like String or byte[]
 * </pre>
 */
public final class JsonDeserializerOfObject {

    /**
     * @return result of parsing JSON to a given class
     */
    public <T> T to(Class<T> clazz) throws IOException {
        @SuppressWarnings("unchecked")
        T result = (T) deserializer.apply(Json.mapper().readerFor(clazz));
        return result;
    }

    /**
     * @return result of parsing JSON to a given type
     */
    public <T> T to(TypeReference<T> type) throws IOException {
        @SuppressWarnings("unchecked")
        T result = (T) deserializer.apply(Json.mapper().readerFor(type));
        return result;
    }

    /**
     * @return result of parsing JSON to a given JavaType
     */
    public <T> T to(JavaType javaType) throws IOException {
        @SuppressWarnings("unchecked")
        T result = (T) deserializer.apply(Json.mapper().readerFor(javaType));
        return result;
    }

    // CONSTRUCTORS

    public JsonDeserializerOfObject(IOFunction<ObjectReader, ?> deserializer) {
        this.deserializer = deserializer;
    }

    // PRIVATE

    private final IOFunction<ObjectReader, ?> deserializer;

}
