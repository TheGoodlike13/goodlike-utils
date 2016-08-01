package eu.goodlike.libraries.jackson.custom.serializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import eu.goodlike.libraries.jackson.Json;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RawSerializerTest {

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToString implements RawSerializable<String> {
        @Override
        public String asJsonObject() {
            return "Success!";
        }
    }

    @Test
    public void serializationToStringWorks() throws IOException {
        SerializableToString serializable = new SerializableToString();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom("Success!"));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToBoolean implements BooleanRawSerializable {
        @Override
        public boolean asJsonBoolean() {
            return true;
        }
    }

    @Test
    public void serializationToBooleanWorks() throws IOException {
        SerializableToBoolean serializable = new SerializableToBoolean();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom(true));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToByte implements ByteRawSerializable {
        @Override
        public byte asJsonByte() {
            return (byte) 200;
        }
    }

    @Test
    public void serializationToByteWorks() throws IOException {
        SerializableToByte serializable = new SerializableToByte();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom((byte) 200));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToChar implements CharRawSerializable {
        @Override
        public char asJsonChar() {
            return (char) 200;
        }
    }

    @Test
    public void serializationToCharWorks() throws IOException {
        SerializableToChar serializable = new SerializableToChar();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom((char) 200));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToDouble implements DoubleRawSerializable {
        @Override
        public double asJsonDouble() {
            return 200;
        }
    }

    @Test
    public void serializationToDoubleWorks() throws IOException {
        SerializableToDouble serializable = new SerializableToDouble();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom((double) 200));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToFloat implements FloatRawSerializable {
        @Override
        public float asJsonFloat() {
            return 200;
        }
    }

    @Test
    public void serializationToFloatWorks() throws IOException {
        SerializableToFloat serializable = new SerializableToFloat();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom((float) 200));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToInt implements IntRawSerializable {
        @Override
        public int asJsonInt() {
            return 200;
        }
    }

    @Test
    public void serializationToIntWorks() throws IOException {
        SerializableToInt serializable = new SerializableToInt();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom(200));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToLong implements LongRawSerializable {
        @Override
        public long asJsonLong() {
            return 200;
        }
    }

    @Test
    public void serializationToLongWorks() throws IOException {
        SerializableToLong serializable = new SerializableToLong();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom((long) 200));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToShort implements ShortRawSerializable {
        @Override
        public short asJsonShort() {
            return 200;
        }
    }

    @Test
    public void serializationToShortWorks() throws IOException {
        SerializableToShort serializable = new SerializableToShort();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom((short) 200));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToIntArray implements RawSerializable<int[]> {
        @Override
        public int[] asJsonObject() {
            return new int[] {200};
        }
    }

    @Test
    public void serializationToArrayWorks() throws IOException {
        SerializableToIntArray serializable = new SerializableToIntArray();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom(new int[] {200}));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToListOfStrings implements RawSerializable<List<String>> {
        @Override
        public List<String> asJsonObject() {
            return ImmutableList.of("Success!");
        }
    }

    @Test
    public void serializationToListWorks() throws IOException {
        SerializableToListOfStrings serializable = new SerializableToListOfStrings();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom(ImmutableList.of("Success!")));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToSetOfStrings implements RawSerializable<Set<String>> {
        @Override
        public Set<String> asJsonObject() {
            return ImmutableSet.of("Success!");
        }
    }

    @Test
    public void serializationToSetWorks() throws IOException {
        SerializableToSetOfStrings serializable = new SerializableToSetOfStrings();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom(ImmutableSet.of("Success!")));
    }

    private static final class JustSerializable {
        @JsonProperty("key")
        public String getKey() {
            return value;
        }

        // CONSTRUCTORS

        private JustSerializable(String value) {
            this.value = value;
        }

        // PRIVATE

        private final String value;
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToSimpleObject implements RawSerializable<JustSerializable> {
        @Override
        public JustSerializable asJsonObject() {
            return new JustSerializable("Success!");
        }
    }

    @Test
    public void serializationToJustSimpleObjectWorks() throws IOException {
        SerializableToSimpleObject serializable = new SerializableToSimpleObject();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom(new JustSerializable("Success!")));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToOtherRawSerializable implements RawSerializable<SerializableToSimpleObject> {
        @Override
        public SerializableToSimpleObject asJsonObject() {
            return new SerializableToSimpleObject();
        }
    }

    @Test
    public void serializationToAnotherRawSerializableWorks() throws IOException {
        SerializableToOtherRawSerializable serializable = new SerializableToOtherRawSerializable();

        assertThat(Json.stringFrom(serializable))
                .isEqualTo(Json.stringFrom(new JustSerializable("Success!")));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class SerializableToObject implements RawSerializable<Object> {
        @Override
        public Object asJsonObject() {
            return new Object();
        }
    }

    @Test
    public void serializationToNonSerializableDoesNotWork() {
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> Json.stringFrom(new SerializableToObject()));
    }

    @JsonSerialize(using = RawSerializer.class)
    private static final class NotRawSerializable {
        public Object toJsonObject() {
            return "Where did the interface go?";
        }
    }

    @Test
    public void serializationOfNotSerializableDoesNotWork() {
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> Json.stringFrom(new NotRawSerializable()));
    }

}