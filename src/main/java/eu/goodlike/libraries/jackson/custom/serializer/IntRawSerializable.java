package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON int
 */
public interface IntRawSerializable extends RawSerializable<Integer> {

    /**
     * @return int that represents this object in JSON
     */
    int toJsonInt();

    /**
     * Cast toJsonBoolean() to Integer; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive toJson...() function
     */
    default Integer toJsonObject() {
        return toJsonInt();
    }

}
