package eu.goodlike.libraries.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.tools.reflect.Reflect;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonMapperTest {

    private final JsonObject jsonObject = new JsonObject();
    private final String jsonString = jsonObject.toString();
    private final byte[] jsonBytes = jsonObject.getBytes();
    private final ObjectMapper mapper = JsonMapper.newMapper();

    @Test
    public void tryDefaultMapper_shouldNotBeSingleton() throws NoSuchFieldException, IllegalAccessException {
        ObjectMapper reflected = Reflect.on(JsonMapper.MAPPER).get("mapper");
        assertThat(JsonMapper.newMapper()).isNotSameAs(reflected);
    }

    @Test
    public void tryWriteAsBytes_shouldReturnBytes() throws IOException {
        assertThat(JsonMapper.MAPPER.writeValueAsBytes(jsonObject)).isEqualTo(jsonBytes);
    }

    @Test
    public void tryWriteAsString_shouldReturnString() throws IOException {
        assertThat(JsonMapper.MAPPER.writeValueAsString(jsonObject)).isEqualTo(jsonString);
    }

    @Test
    public void tryReaderForClass_shouldParseAsAnyMapper() throws IOException {
        JsonObject o = JsonMapper.MAPPER.readerFor(JsonObject.class).readValue(jsonString);
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryReaderForTypeReference_shouldParseAsAnyMapper() throws IOException {
        JsonObject o = JsonMapper.MAPPER.readerFor(JsonObject.getTypeReference()).readValue(jsonString);
        assertThat(o).isEqualTo(jsonObject);
    }

    @Test
    public void tryReaderForJavaType_shouldParseAsAnyMapper() throws IOException {
        JsonObject o = JsonMapper.MAPPER.readerFor(JsonObject.getJavaType()).readValue(jsonString);
        assertThat(o).isEqualTo(jsonObject);
    }

}
