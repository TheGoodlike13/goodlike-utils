package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON char
 */
public interface CharRawSerializable extends RawSerializable<Character> {

    /**
     * @return char that represents this object in JSON
     */
    char asJsonChar();

    /**
     * Cast asJsonChar() to Character; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive asJson...() function
     */
    default Character asJsonObject() {
        return asJsonChar();
    }

}
