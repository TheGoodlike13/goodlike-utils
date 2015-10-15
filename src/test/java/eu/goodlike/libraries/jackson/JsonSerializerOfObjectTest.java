package eu.goodlike.libraries.jackson;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSerializerOfObjectTest {

    private final JsonObject jsonObject = new JsonObject();
    private final String jsonString = jsonObject.toString();
    private final byte[] jsonBytes = jsonObject.getBytes();

    private JsonSerializerOfObject jsonSerializer;

    @Before
    public void setup() {
        jsonSerializer = new JsonSerializerOfObject(jsonObject);
    }

    @Test
    public void tryAsString_shouldReturnString() throws IOException {
        assertThat(jsonSerializer.asString()).isEqualTo(jsonString);
    }

    @Test
    public void tryAsBytes_shouldReturnBytes() throws IOException {
        assertThat(jsonSerializer.asBytes()).isEqualTo(jsonBytes);
    }

}
