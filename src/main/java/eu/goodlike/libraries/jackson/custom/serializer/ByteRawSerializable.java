package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON byte
 */
public interface ByteRawSerializable extends RawSerializable<Byte> {

    /**
     * @return byte that represents this object in JSON
     */
    byte toJsonByte();

    /**
     * Cast toJsonByte() to Byte; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive toJson...() function
     */
    default Byte toJsonObject() {
        return toJsonByte();
    }

}
