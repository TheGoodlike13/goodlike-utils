package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON short
 */
public interface ShortRawSerializable extends RawSerializable<Short> {

    /**
     * @return short that represents this object in JSON
     */
    short asJsonShort();

    /**
     * Cast asJsonShort() to Short; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive asJson...() function
     */
    default Short asJsonObject() {
        return asJsonShort();
    }

}
