package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;
import org.jooq.lambda.Unchecked;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.UUID;

import static eu.goodlike.misc.Constants.DEFAULT_CHARSET;
import static eu.goodlike.misc.Constants.DEF_CHARSET;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonTest {

    private final JsonObject jsonObject = new JsonObject();
    private final String jsonString = jsonObject.toString();
    private final byte[] jsonBytes = Unchecked.supplier(() -> jsonString.getBytes(DEFAULT_CHARSET)).get();

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
        JsonObject fromClass = Json.read(JsonObject.getTypeReference()).from(jsonString);
        assertThat(fromClass).isEqualTo(new JsonDeserializerForType(Json.mapper().readerFor(JsonObject.getTypeReference())).from(jsonString));
    }

    @Test
    public void tryReadJavaType_shouldUseConstructor() throws IOException {
        JsonObject fromClass = Json.read(JsonObject.getJavaType()).from(jsonString);
        assertThat(fromClass).isEqualTo(new JsonDeserializerForType(Json.mapper().readerFor(JsonObject.getJavaType())).from(jsonString));
    }

    @Test
    public void tryFromInputStream_shouldUseConstructor() throws IOException {
        InputStream jsonInputSteam1 = new ByteArrayInputStream(jsonBytes);
        InputStream jsonInputSteam2 = new ByteArrayInputStream(jsonBytes);
        assertThat(Json.from(jsonInputSteam1).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(jsonInputSteam2)).to(JsonObject.class));
    }

    @Test
    public void tryFromReader_shouldUseConstructor() throws IOException {
        Reader jsonReader1 = new StringReader(jsonString);
        Reader jsonReader2 = new StringReader(jsonString);
        assertThat(Json.from(jsonReader1).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(jsonReader2)).to(JsonObject.class));
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
        byte[] jsonBytesCopy = new byte[jsonBytes.length + 2];
        System.arraycopy(jsonBytes, 0, jsonBytesCopy, 1, jsonBytes.length);
        assertThat(Json.from(jsonBytesCopy, 1, jsonBytes.length).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(jsonBytesCopy, 1, jsonBytes.length)).to(JsonObject.class));
    }

    @Test
    public void tryFromFile_shouldUseConstructor() throws IOException {
        File file = File.createTempFile(UUID.randomUUID().toString(), ".txt");
        Files.write(jsonString, file, DEF_CHARSET);
        assertThat(Json.from(file).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(file)).to(JsonObject.class));
        file.deleteOnExit();
    }

    @Test
    public void tryFromURL_shouldUseConstructor() throws IOException {
        File file = File.createTempFile(UUID.randomUUID().toString(), ".txt");
        Files.write(jsonString, file, DEF_CHARSET);
        URL url = file.toURI().toURL();
        assertThat(Json.from(url).to(JsonObject.class))
                .isEqualTo(new JsonDeserializerOfObject(reader -> reader.readValue(url)).to(JsonObject.class));
        file.deleteOnExit();
    }

    @Test
    public void tryFromJsonNode_shouldUseConstructor() throws IOException {
        JsonNode node = JsonMapper.defaultMapper().valueToTree(jsonObject);
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
