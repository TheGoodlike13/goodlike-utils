package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonTest {

    private final JsonObject jsonObject = new JsonObject();
    private final String jsonString = jsonObject.toString();
    private final byte[] jsonBytes = jsonObject.getBytes();
    private final File tempFile = jsonObject.createTempFile();

    @Test
    public void tryMapper_shouldBeSingleton() {
        assertThat(Json.mapper()).isSameAs(JsonMapper.MAPPER);
    }

    @Test
    public void tryReadClass_shouldUseConstructor() throws IOException {
        JsonObject fromClass = Json.read(JsonObject.class).from(jsonString);
        assertThat(fromClass).isEqualTo(new JsonDeserializerForType(Json.mapper().readerFor(JsonObject.class)).from(jsonString));
    }

    @Test
    public void tryReadTypeReference_shouldUseConstructor() throws IOException {
        JsonObject fromType = Json.read(JsonObject.getTypeReference()).from(jsonString);
        assertThat(fromType).isEqualTo(new JsonDeserializerForType(Json.mapper().readerFor(JsonObject.getTypeReference())).from(jsonString));
    }

    @Test
    public void tryReadJavaType_shouldUseConstructor() throws IOException {
        JsonObject fromType = Json.read(JsonObject.getJavaType()).from(jsonString);
        assertThat(fromType).isEqualTo(new JsonDeserializerForType(Json.mapper().readerFor(JsonObject.getJavaType())).from(jsonString));
    }

    @Test
    public void tryFromInputStream_shouldUseConstructor() throws IOException {
        assertThat(Json.from(jsonObject.newInputStream()).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(jsonObject.newInputStream())).to(JsonObject.class));
    }

    @Test
    public void tryFromReader_shouldUseConstructor() throws IOException {
        assertThat(Json.from(jsonObject.newReader()).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(r -> r.readValue(jsonObject.newReader())).to(JsonObject.class));
    }

    @Test
    public void tryFromString_shouldUseConstructor() throws IOException {
        assertThat(Json.from(jsonString).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(jsonString)).to(JsonObject.class));
    }

    @Test
    public void tryFromBytes_shouldUseConstructor() throws IOException {
        assertThat(Json.from(jsonBytes).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(jsonBytes)).to(JsonObject.class));
    }

    @Test
    public void tryFromBytesOffset_shouldUseConstructor() throws IOException {
        byte[] jsonBytesCopy = jsonObject.getBytesWithPaddingOf1();
        assertThat(Json.from(jsonBytesCopy, 1, jsonBytes.length).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(r -> r.readValue(jsonBytesCopy, 1, jsonBytes.length)).to(JsonObject.class));
    }

    @Test
    public void tryFromFile_shouldUseConstructor() throws IOException {
        assertThat(Json.from(tempFile).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(tempFile)).to(JsonObject.class));
    }

    @Test
    public void tryFromURL_shouldUseConstructor() throws IOException {
        URL url = tempFile.toURI().toURL();
        assertThat(Json.from(url).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(url)).to(JsonObject.class));
    }

    @Test
    public void tryFromJsonNode_shouldUseConstructor() throws IOException {
        JsonNode node = jsonObject.toJsonNode();
        assertThat(Json.from(node).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(node)).to(JsonObject.class));
    }

    @Test
    public void tryString_shouldBeSingleton() {
        assertThat(Json.string()).isSameAs(JsonSerializerToString.STRING_SERIALIZER);
    }

    @Test
    public void tryBytes_shouldBeSingleton() {
        assertThat(Json.bytes()).isSameAs(JsonSerializerToBytes.BYTES_SERIALIZER);
    }

    @Test
    public void tryStringFrom_shouldReturnJsonString() throws IOException {
        assertThat(Json.stringFrom(jsonObject)).isEqualTo(jsonString);
    }

    @Test
    public void tryBytesFrom_shouldReturnJsonBytes() throws IOException {
        assertThat(Json.bytesFrom(jsonObject)).isEqualTo(jsonBytes);
    }

    @Test
    public void tryWrite_shouldUseConstructor() {
        assertThat(Json.write(jsonObject)).isEqualTo(new JsonSerializerOfObject(jsonObject));
    }

}
