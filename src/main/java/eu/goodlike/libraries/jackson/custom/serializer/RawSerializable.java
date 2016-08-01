package eu.goodlike.libraries.jackson.custom.serializer;

/**
 * <pre>
 * Describes how to serialize an object into another object that can be serialized used Jackson
 *
 * This allows for simple uses, such as serializing an object into a String
 *
 * Beware that the objects you serialize do not have any circular dependencies, i.e.
 *      {@literal T implements RawSerializable<U>}
 *      {@literal U implements RawSerializable<T>}
 * will cause an infinite loop, because the serializer will never be able to arrive at an actual implementation to serialize
 * </pre>
 */
public interface RawSerializable<T> {

    /**
     * @return object, that represents this object and Jackson can serialize
     */
    T asJsonObject();

}
