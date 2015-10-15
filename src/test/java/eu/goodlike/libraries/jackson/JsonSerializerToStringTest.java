package eu.goodlike.libraries.jackson;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSerializerToStringTest {

    private final JsonObject jsonObject = new JsonObject();
    private final String jsonString = jsonObject.toString();

    @Test
    public void try_shouldReturnString() throws IOException {
        assertThat(JsonSerializerToString.STRING_SERIALIZER.from(jsonObject)).isEqualTo(jsonString);
    }

}
