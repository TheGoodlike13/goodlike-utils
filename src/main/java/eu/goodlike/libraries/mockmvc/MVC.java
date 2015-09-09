package eu.goodlike.libraries.mockmvc;

import eu.goodlike.libraries.jackson.Json;
import eu.goodlike.libraries.jackson.JsonDeserializerOfObject;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

/**
 * <pre>
 * Contains 2 methods:
 * 1) wrapper creation for MockMvc
 * 2) delegation of MvcResult parsing as JSON
 * </pre>
 */
public final class MVC {

    public static MVCMock mock(MockMvc mockMvc) {
        return new MVCMock(mockMvc);
    }

    public static JsonDeserializerOfObject from(MvcResult result) throws IOException {
        return Json.from(result.getResponse().getContentAsByteArray());
    }

    // PRIVATE

    private MVC() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
