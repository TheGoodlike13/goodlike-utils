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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static eu.goodlike.misc.Constants.DEF_CHARSET;

/**
 * Simple object which used Jackson annotations; used only in testing
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class JsonObject {

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public Instant getTime() {
        return time;
    }

    @JsonProperty
    public Path getPath() {
        return path;
    }

    @JsonProperty
    public Optional<String> getOptional() {
        return optional;
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
        return JsonMapper.newMapper().valueToTree(this);
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
    public JsonObject(int id, Instant time, Path path, Optional<String> optional) {
        this.id = id;
        this.time = time;
        this.path = path;
        this.optional = optional;
        this.string = "{\"id\":" + id + "," +
                "\"time\":\"" + time + "\"," +
                "\"path\":\"" + (path == null ? null : path.toUri()) + "\"," +
                "\"optional\":\"" + (optional.isPresent() ? optional.get() : null) + "\"}";
        this.bytes = this.string.getBytes(DEF_CHARSET);
    }

    public JsonObject() {
        this(1, Instant.now(), Paths.get("here"), Optional.of("a"));
    }

    // PRIVATE

    private final int id;
    private final Instant time;
    private final Path path;
    private final Optional<String> optional;
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
