package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * Describes how to serialize an object into a JSON double
 */
public interface DoubleRawSerializable extends RawSerializable<Double> {

    /**
     * @return double that represents this object in JSON
     */
    double toJsonDouble();

    /**
     * Cast toJsonDouble() to Double; any performance loss is negligible, but allows to reuse RawSerializer while still
     * allowing to define primitive toJson...() function
     */
    default Double toJsonObject() {
        return toJsonDouble();
    }

}
