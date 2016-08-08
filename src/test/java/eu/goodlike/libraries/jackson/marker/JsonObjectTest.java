package eu.goodlike.libraries.jackson.marker;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JsonObjectTest {

    private static final class JsonExample implements JsonObject {
        @JsonProperty("some_key")
        public String getValueForSomeKey() {
            return "some_value";
        }
    }

    @Test
    public void toJsonUsesJacksonToConvert() {
        String expectedJson = "{\"some_key\":\"some_value\"}";

        assertThat(new JsonExample().toJson())
                .isEqualTo(expectedJson);
    }

    private static final class NotJsonExample implements JsonObject {

    }

    @Test
    public void noJacksonNotAllowedMarkerInterface() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> new NotJsonExample().toJson());
    }

}