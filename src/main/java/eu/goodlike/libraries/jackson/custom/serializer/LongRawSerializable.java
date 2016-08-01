package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON long
 */
public interface LongRawSerializable extends RawSerializable<Long> {

    /**
     * @return long that represents this object in JSON
     */
    long asJsonLong();

    /**
     * Cast asJsonLong() to Long; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive asJson...() function
     */
    default Long asJsonObject() {
        return asJsonLong();
    }

}
