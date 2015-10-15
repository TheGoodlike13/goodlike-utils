package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.io.Files;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

import static eu.goodlike.misc.Constants.DEF_CHARSET;

/**
 * Simple object which used Jackson annotations; used only in testing
 */
public final class JsonObject {

    @JsonProperty(value = "id")
    public int getId() {
        return id;
    }

    @JsonIgnore
    public byte[] getBytes() {
        return bytes;
    }

    @JsonIgnore
    public byte[] getBytesWithPaddingOf1() {
        byte[] jsonBytesCopy = new byte[bytes.length + 2];
        System.arraycopy(bytes, 0, jsonBytesCopy, 1, bytes.length);
        return jsonBytesCopy;
    }

    @JsonIgnore
    public static TypeReference<JsonObject> getTypeReference() {
        return JSON_OBJECT_TYPE;
    }

    @JsonIgnore
    public static JavaType getJavaType() {
        return JSON_OBJECT_JAVA_TYPE;
    }

    @JsonIgnore
    public InputStream newInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @JsonIgnore
    public Reader newReader() {
        return new StringReader(string);
    }

    @JsonIgnore
    public JsonNode toJsonNode() {
        return JsonMapper.defaultMapper().valueToTree(this);
    }

    @JsonIgnore
    public File createTempFile() {
        try {
            File file = File.createTempFile(UUID.randomUUID().toString(), ".txt");
            Files.write(toString(), file, DEF_CHARSET);
            file.deleteOnExit();
            return file;
        } catch (IOException e) {
            throw new AssertionError("The odds of this failing are cosmologically small", e);
        }
    }

    // CONSTRUCTORS

    @JsonCreator
    public JsonObject(@JsonProperty(value = "id") int id) {
        this.id = id;
        this.string = "{\"id\":" + id + "}";
        this.bytes = this.string.getBytes(DEF_CHARSET);
    }

    public JsonObject() {
        this(1);
    }

    // PRIVATE

    private final int id;
    private final String string;
    private final byte[] bytes;

    private static final TypeReference<JsonObject> JSON_OBJECT_TYPE = new TypeReference<JsonObject>() {};
    private static final JavaType JSON_OBJECT_JAVA_TYPE = TypeFactory.defaultInstance().constructType(JsonObject.class);

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonObject)) return false;
        JsonObject that = (JsonObject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
