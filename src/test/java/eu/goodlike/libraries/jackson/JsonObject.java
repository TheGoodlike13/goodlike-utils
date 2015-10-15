package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.Objects;

/**
 * Simple object which used Jackson annotations; used only in testing
 */
public final class JsonObject {

    @JsonProperty(value = "id")
    public int getId() {
        return id;
    }

    public static TypeReference<JsonObject> getTypeReference() {
        return JSON_OBJECT_TYPE;
    }

    public static JavaType getJavaType() {
        return JSON_OBJECT_JAVA_TYPE;
    }

    // CONSTRUCTORS

    @JsonCreator
    public JsonObject(@JsonProperty(value = "id") int id) {
        this.id = id;
    }

    public JsonObject() {
        this(1);
    }

    // PRIVATE

    private final int id;

    private static final TypeReference<JsonObject> JSON_OBJECT_TYPE = new TypeReference<JsonObject>() {};
    private static final JavaType JSON_OBJECT_JAVA_TYPE = TypeFactory.defaultInstance().constructType(JsonObject.class);

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return "{\"id\":" + id + "}";
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
