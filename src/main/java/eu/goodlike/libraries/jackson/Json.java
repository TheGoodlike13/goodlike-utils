package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * Allows for simple and fluent JSON conversion
 */
public final class Json {

    /**
     * @return a singleton wrapper for ObjectMapper; it uses default settings, with the exception that
     * it always includes nulls
     */
    public static JsonMapper mapper() {
        return JsonMapper.MAPPER;
    }

    /**
     * Json.read(JsonNode.class) also works, so, to avoid confusion (normally deserializer takes an annotated class),
     * this method is provided separately
     * @return JSON deserializer for JsonNode
     */
    public static JsonDeserializerForType<JsonNode> readNode() throws IOException {
        return read(JsonNode.class);
    }

    /**
     * @return JSON deserializer for a class
     */
    public static <T> JsonDeserializerForType<T> read(Class<T> clazz) throws IOException {
        return new JsonDeserializerForType<>(mapper().readerFor(clazz));
    }

    /**
     * @return JSON deserializer for a type
     */
    public static <T> JsonDeserializerForType<T> read(TypeReference<T> type) throws IOException {
        return new JsonDeserializerForType<>(mapper().readerFor(type));
    }

    /**
     * @return JSON deserializer for an input stream
     */
    public static JsonDeserializerOfObject from(InputStream inputStream) {
        return new JsonDeserializerOfObject(reader -> reader.readValue(inputStream));
    }

    /**
     * @return JSON deserializer for a reader
     */
    public static JsonDeserializerOfObject from(Reader reader) {
        return new JsonDeserializerOfObject(objectReader -> objectReader.readValue(reader));
    }

    /**
     * @return JSON deserializer for a string
     */
    public static JsonDeserializerOfObject from(String string) {
        return new JsonDeserializerOfObject(reader -> reader.readValue(string));
    }

    /**
     * @return JSON deserializer for a byte array
     */
    public static JsonDeserializerOfObject from(byte[] bytes) {
        return new JsonDeserializerOfObject(reader -> reader.readValue(bytes));
    }

    /**
     * @return JSON deserializer for a part of byte array, using offset and length
     */
    public static JsonDeserializerOfObject from(byte[] bytes, int offset, int length) {
        return new JsonDeserializerOfObject(reader -> reader.readValue(bytes, offset, length));
    }

    /**
     * @return JSON deserializer for a file
     */
    public static JsonDeserializerOfObject from(File file) {
        return new JsonDeserializerOfObject(reader -> reader.readValue(file));
    }

    /**
     * @return JSON deserializer for an URL
     */
    public static JsonDeserializerOfObject from(URL url) {
        return new JsonDeserializerOfObject(reader -> reader.readValue(url));
    }

    /**
     * @return JSON deserializer for a JsonNode
     */
    public static JsonDeserializerOfObject from(JsonNode jsonNode) {
        return new JsonDeserializerOfObject(reader -> reader.readValue(jsonNode));
    }

    /**
     * @return JSON serializer for strings
     */
    public static JsonSerializerToString string() {
        return JsonSerializerToString.STRING_SERIALIZER;
    }

    /**
     * @return JSON serializer for byte arrays
     */
    public static JsonSerializerToBytes bytes() {
        return JsonSerializerToBytes.BYTES_SERIALIZER;
    }

    /**
     * @return JSON string, parsed from given object
     */
    public static String stringFrom(Object o) throws IOException {
        return JsonSerializerToString.STRING_SERIALIZER.from(o);
    }

    /**
     * @return JSON byte array, parsed from given object
     */
    public static byte[] bytesFrom(Object o) throws IOException {
        return JsonSerializerToBytes.BYTES_SERIALIZER.from(o);
    }

    /**
     * @return JSON serializer for an object
     */
    public static JsonSerializerOfObject write(Object o) {
        return new JsonSerializerOfObject(o);
    }

    // PRIVATE

    private Json() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
