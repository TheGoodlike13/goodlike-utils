package eu.goodlike.libraries.jackson;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonDeserializerOfObjectTest {

    private final JsonObject jsonObject = new JsonObject();

    private JsonDeserializerOfObject jsonDeserializer;

    @Before
    public void setup() {
        jsonDeserializer = new JsonDeserializerOfObject(reader -> reader.readValue(jsonObject.toString()));
    }

    @Test
    public void tryToClass_shouldReturnObject() throws IOException {
        assertThat(jsonDeserializer.to(JsonObject.class)).isEqualTo(jsonObject);
    }

    @Test
    public void tryToTypeReference_shouldReturnObject() throws IOException {
        assertThat(jsonDeserializer.to(JsonObject.getTypeReference())).isEqualTo(jsonObject);
    }

    @Test
    public void tryToJavaType_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.to(JsonObject.getJavaType());
        assertThat(o).isEqualTo(jsonObject);
    }

}
