package eu.goodlike.libraries.jackson.custom.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * <pre>
 * Serializes any RawSerializable as any other object, that can be serialized using Jackson
 *
 * This allows for simple uses, such as serializing an object into a String
 *
 * Beware that the objects you serialize do not have any circular dependencies, i.e.
 *      {@literal T implements RawSerializable<U>}
 *      {@literal U implements RawSerializable<T>}
 * will cause an infinite loop, because the serializer will never be able to arrive at an actual implementation to serialize
 * </pre>
 */
public final class RawSerializer extends StdSerializer<RawSerializable<?>> {

    @Override
    public void serialize(RawSerializable value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(value.toJsonObject());
    }

    // CONSTRUCTORS

    public RawSerializer() {
        this(null);
    }

    public RawSerializer(Class<RawSerializable<?>> t) {
        super(t);
    }

}
