package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON byte
 */
public interface ByteRawSerializable extends RawSerializable<Byte> {

    /**
     * @return byte that represents this object in JSON
     */
    byte asJsonByte();

    /**
     * Cast asJsonByte() to Byte; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive asJson...() function
     */
    default Byte asJsonObject() {
        return asJsonByte();
    }

}
