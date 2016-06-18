package eu.goodlike.libraries.spring.mockmvc;

import eu.goodlike.libraries.jackson.Json;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <pre>
 * Wraps MockMvc with a lot of convenience methods for a basic testing setup:
 *
 * a) Can have or not have Auth header;
 * b) if it returns OK with body, checked for JSON type
 * c) if it returns OK without body, no content type check
 * d) if it returns something else, only custom checks
 * </pre>
 */
public final class MVCMock {

    /**
     * @return new MockMvc wrapper, using the same MockMvc instance, but a different auth token
     */
    public MVCMock withAuth(String authToken) {
        return new MVCMock(this.mockMvc, authToken);
    }

    /**
     * Performs a HTTP GET request to given path; expects HttpResult and based on it, uses ResultMatchers
     * @return result of this request, if it was successful
     */
    public Optional<MvcResult> performGet(String path, HttpResult httpResult,
                                             ResultMatcher... expect) throws Exception {
        return performRequest(this::get, path, httpResult, expect);
    }

    /**
     * Performs a HTTP POST request to given path using given body; expects HttpResult and based on it, uses ResultMatchers
     * @return result of this request, if it was successful
     */
    public Optional<MvcResult> performPost(String path, Object body, HttpResult httpResult,
                                              ResultMatcher... expect) throws Exception {
        return performRequest(this::post, path, body, httpResult, expect);
    }

    /**
     * Performs a HTTP PUT request to given path using given body; expects HttpResult and based on it, uses ResultMatchers
     * @return result of this request, if it was successful
     */
    public Optional<MvcResult> performPut(String path, Object body, HttpResult httpResult,
                                             ResultMatcher... expect) throws Exception {
        return performRequest(this::put, path, body, httpResult, expect);
    }

    /**
     * Performs a HTTP DELETE request to given path; expects HttpResult and based on it, uses ResultMatchers
     * @return result of this request, if it was successful
     */
    public Optional<MvcResult> performDelete(String path, HttpResult httpResult,
                                                ResultMatcher... expect) throws Exception {
        return performRequest(this::delete, path, httpResult, expect);
    }

    /**
     * Performs a HTTP request (without body) to given path; expects HttpResult and based on it, uses ResultMatchers
     * @return result of this request, if it was successful
     */
    public Optional<MvcResult> performRequest(Function<String, MockHttpServletRequestBuilder> requestType, String path,
                                                 HttpResult httpResult, ResultMatcher... expect) throws Exception {
        ResultActions resultActions = mockMvc.perform(requestType.apply(path)).andDo(print());
        return handleExpects(resultActions, httpResult, expect);
    }

    /**
     * Performs a HTTP request (with body) to given path; expects HttpResult and based on it, uses ResultMatchers
     * @return result of this request, if it was successful
     */
    public Optional<MvcResult> performRequest(BiFunction<String, Object, MockHttpServletRequestBuilder> requestType, String path, Object body,
                                                 HttpResult httpResult, ResultMatcher... expect) throws Exception {
        ResultActions resultActions = mockMvc.perform(requestType.apply(path, body)).andDo(print());
        return handleExpects(resultActions, httpResult, expect);
    }

    /**
     * @return basic GET request
     */
    public MockHttpServletRequestBuilder get(String path) {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(path)
                .contentType(APPLICATION_JSON_UTF8);

        if (authToken != null)
            builder = builder.header("Authorization", authToken);

        return builder;
    }

    /**
     * @return basic POST request
     */
    public MockHttpServletRequestBuilder post(String path, Object body) {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(path)
                .contentType(APPLICATION_JSON_UTF8);

        if (authToken != null)
            builder = builder.header("Authorization", authToken);

        if (body != null)
            try {
                builder = builder.content(Json.bytesFrom(body));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        return builder;
    }

    /**
     * @return basic PUT request
     */
    public MockHttpServletRequestBuilder put(String path, Object body) {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(path)
                .contentType(APPLICATION_JSON_UTF8);

        if (authToken != null)
            builder = builder.header("Authorization", authToken);

        if (body != null)
            try {
                builder = builder.content(Json.bytesFrom(body));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        return builder;
    }

    /**
     * @return basic DELETE request
     */
    public MockHttpServletRequestBuilder delete(String path) {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(path)
                .contentType(APPLICATION_JSON_UTF8);

        if (authToken != null)
            builder = builder.header("Authorization", authToken);

        return builder;
    }

    // CONSTRUCTORS

    public MVCMock(MockMvc mockMvc) {
        this(mockMvc, null);
    }

    public MVCMock(MockMvc mockMvc, String authToken) {
        this.mockMvc = mockMvc;
        this.authToken = authToken;
    }

    // PRIVATE

    private final MockMvc mockMvc;
    private final String authToken;

    private Optional<MvcResult> handleExpects(ResultActions resultActions, HttpResult resultVariation, ResultMatcher... expect) throws Exception {
        if (resultVariation != HttpResult.BAD)
            return Optional.of(expect(okActions(resultActions, resultVariation), expect).andReturn());

        expect(resultActions, expect);
        return Optional.empty();
    }

    private ResultActions expect(ResultActions seed, ResultMatcher... expect) {
        ResultActions resultActions = seed;
        for (ResultMatcher resultMatcher : expect)
            try {
                resultActions = resultActions.andExpect(resultMatcher);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        return seed;
    }

    private ResultActions okActions(ResultActions seed, HttpResult resultVariation) throws Exception {
        ResultActions resultActions = seed.andExpect(status().isOk());
        return resultVariation == HttpResult.OK_NO_BODY ? resultActions : resultActions.andExpect(
                content().contentType(APPLICATION_JSON_UTF8));
    }

}
