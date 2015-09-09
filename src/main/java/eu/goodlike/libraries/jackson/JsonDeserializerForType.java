package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * De-serializes JSON using a provided ObjectReader; this reader is usually the result of ObjectMapper::readerFor
 */
public final class JsonDeserializerForType {

    /**
     * @return result of parsing JSON in given input stream
     */
    public <T> T from(InputStream inputStream) throws IOException {
        return reader.readValue(inputStream);
    }

    /**
     * @return result of parsing JSON in given reader
     */
    public <T> T from(Reader reader) throws IOException {
        return this.reader.readValue(reader);
    }

    /**
     * @return result of parsing JSON in given string
     */
    public <T> T from(String string) throws IOException {
        return reader.readValue(string);
    }

    /**
     * @return result of parsing JSON in given byte array
     */
    public <T> T from(byte[] bytes) throws IOException {
        return reader.readValue(bytes);
    }

    /**
     * @return result of parsing JSON in given byte array, starting at offset, taking length worth of bytes
     */
    public <T> T from(byte[] bytes, int offset, int length) throws IOException {
        return reader.readValue(bytes, offset, length);
    }

    /**
     * @return result of parsing JSON in given file
     */
    public <T> T from(File file) throws IOException {
        return reader.readValue(file);
    }

    /**
     * @return result of parsing JSON in given URL
     */
    public <T> T from(URL url) throws IOException {
        return reader.readValue(url);
    }

    /**
     * @return result of parsing JSON in given JsonNode
     */
    public <T> T from(JsonNode jsonNode) throws IOException {
        return reader.readValue(jsonNode);
    }

    // CONSTRUCTORS

    public JsonDeserializerForType(ObjectReader reader) {
        this.reader = reader;
    }

    // PRIVATE

    private final ObjectReader reader;

}
