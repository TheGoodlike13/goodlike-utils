package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON boolean
 */
public interface BooleanRawSerializable extends RawSerializable<Boolean> {

    /**
     * @return boolean that represents this object in JSON
     */
    boolean toJsonBoolean();

    /**
     * Cast toJsonBoolean() to Boolean; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive toJson...() function
     */
    default Boolean toJsonObject() {
        return toJsonBoolean();
    }

}
