package eu.goodlike.libraries.jackson;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonDeserializerForTypeTest {

    private final JsonObject jsonObject = new JsonObject();
    private final byte[] jsonBytes = jsonObject.getBytes();
    private final File tempFile = jsonObject.createTempFile();

    private JsonDeserializerForType jsonDeserializer;

    @Before
    public void setup() throws IOException {
        jsonDeserializer = new JsonDeserializerForType(Json.mapper().readerFor(JsonObject.class));
    }

    @Test
    public void tryFromInputStream_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.from(jsonObject.newInputStream());
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryFromReader_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.from(jsonObject.newReader());
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryFromString_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.from(jsonObject.toString());
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryFromBytes_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.from(jsonObject.getBytes());
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryFromBytesWithOffset_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.from(jsonObject.getBytesWithPaddingOf1(), 1, jsonBytes.length);
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryFromFile_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.from(tempFile);
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryFromURL_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.from(tempFile.toURI().toURL());
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryFromJsonNode_shouldReturnObject() throws IOException {
        JsonObject o = jsonDeserializer.from(jsonObject.toJsonNode());
        assertThat(o).isEqualTo(jsonObject);
    }

}
