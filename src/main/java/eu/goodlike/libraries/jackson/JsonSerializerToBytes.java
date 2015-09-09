package eu.goodlike.libraries.jackson;

import java.io.IOException;

/**
 * Serializes an Object into JSON byte array
 */
public enum JsonSerializerToBytes {

    BYTES_SERIALIZER;

    /**
     * @return JSON byte array representation of given object
     */
    public byte[] from(Object o) throws IOException {
        return Json.mapper().writeValueAsBytes(o);
    }

}
