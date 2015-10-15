package eu.goodlike.libraries.jackson;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSerializerToBytesTest {

    private final JsonObject jsonObject = new JsonObject();
    private final byte[] jsonBytes = jsonObject.getBytes();

    @Test
    public void try_shouldReturnBytes() throws IOException {
        assertThat(JsonSerializerToBytes.BYTES_SERIALIZER.from(jsonObject)).isEqualTo(jsonBytes);
    }

}
