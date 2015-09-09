package eu.goodlike.libraries.jackson;

import java.io.IOException;

/**
 * Serializes an Object into JSON string
 */
public enum JsonSerializerToString {

    STRING_SERIALIZER;

    /**
     * @return JSON string representation of given object
     */
    public String from(Object o) throws IOException {
        return Json.mapper().writeValueAsString(o);
    }

}
