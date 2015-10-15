package eu.goodlike.libraries.jackson;

import java.io.IOException;
import java.util.Objects;

/**
 * Serializes an Object into JSON
 */
public final class JsonSerializerOfObject {

    /**
     * @return JSON string representation of the object
     */
    public String asString() throws IOException {
        return Json.stringFrom(o);
    }

    /**
     * @return JSON byte array representation of the object
     */
    public byte[] asBytes() throws IOException {
        return Json.bytesFrom(o);
    }

    // CONSTRUCTORS

    public JsonSerializerOfObject(Object o) {
        this.o = o;
    }

    // PRIVATE

    private final Object o;

    // OBJECT OVERRIDES

    @Override
    public boolean equals(Object o1) {
        if (this == o1) return true;
        if (!(o1 instanceof JsonSerializerOfObject)) return false;
        JsonSerializerOfObject that = (JsonSerializerOfObject) o1;
        return Objects.equals(o, that.o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o);
    }

}
