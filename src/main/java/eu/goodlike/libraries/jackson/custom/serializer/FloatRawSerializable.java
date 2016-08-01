package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON float
 */
public interface FloatRawSerializable extends RawSerializable<Float> {

    /**
     * @return float that represents this object in JSON
     */
    float toJsonFloat();

    /**
     * Cast toJsonFloat() to Float; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive toJson...() function
     */
    default Float toJsonObject() {
        return toJsonFloat();
    }

}
