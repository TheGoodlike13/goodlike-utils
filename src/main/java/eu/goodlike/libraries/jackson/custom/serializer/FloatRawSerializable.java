package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON float
 */
public interface FloatRawSerializable extends RawSerializable<Float> {

    /**
     * @return float that represents this object in JSON
     */
    float asJsonFloat();

    /**
     * Cast asJsonFloat() to Float; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive asJson...() function
     */
    default Float asJsonObject() {
        return asJsonFloat();
    }

}
