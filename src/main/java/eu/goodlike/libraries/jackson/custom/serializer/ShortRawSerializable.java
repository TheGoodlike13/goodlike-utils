package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON short
 */
public interface ShortRawSerializable extends RawSerializable<Short> {

    /**
     * @return short that represents this object in JSON
     */
    short toJsonShort();

    /**
     * Cast toJsonShort() to Short; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive toJson...() function
     */
    default Short toJsonObject() {
        return toJsonShort();
    }

}
