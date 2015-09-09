package eu.goodlike.libraries.jackson;

import java.io.IOException;

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

}
