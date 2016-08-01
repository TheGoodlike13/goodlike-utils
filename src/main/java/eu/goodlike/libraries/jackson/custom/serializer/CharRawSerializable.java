package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON char
 */
public interface CharRawSerializable extends RawSerializable<Character> {

    /**
     * @return char that represents this object in JSON
     */
    char toJsonChar();

    /**
     * Cast toJsonChar() to Character; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive toJson...() function
     */
    default Character toJsonObject() {
        return toJsonChar();
    }

}
