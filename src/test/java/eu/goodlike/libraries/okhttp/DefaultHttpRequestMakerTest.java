package eu.goodlike.libraries.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.goodlike.libraries.jackson.Json;
import okhttp3.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

public class DefaultHttpRequestMakerTest {

    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");
    private static final String BODY_STRING = "body_string";
    private static final TypeReference<String> STRING_TYPE = new TypeReference<String>() {};

    private HttpRequestCaller httpRequestCaller;
    private HttpRequestMaker defaultHttpRequestMaker;

    @Before
    public void setup() throws Exception {
        httpRequestCaller = Mockito.mock(HttpRequestCaller.class);
        defaultHttpRequestMaker = new DefaultHttpRequestMaker(httpRequestCaller);
    }

    private Request newRequest(HttpUrl url) {
        return new Request.Builder().url(url).build();
    }

    private ResponseBody newResponseBody(Object responseBody) throws IOException {
        return ResponseBody.create(MediaType.parse("application/json"), Json.stringFrom(responseBody));
    }

    private Response newResponse(Request request, ResponseBody responseBody) {
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .body(responseBody)
                .message("OK")
                .build();
    }

    @Test
    public void makeHttpRequestForGivenClass() {
        Mockito.when(httpRequestCaller.callRequest(any(Request.class), eq(String.class)))
                .thenReturn(CompletableFuture.completedFuture(BODY_STRING));

        assertThat(defaultHttpRequestMaker.makeRequest(URL, String.class))
                .isCompletedWithValue(BODY_STRING);
    }

    @Test
    public void makeHttpRequestForGivenType() {
        Mockito.when(httpRequestCaller.callRequest(any(Request.class), eq(STRING_TYPE)))
                .thenReturn(CompletableFuture.completedFuture(BODY_STRING));

        assertThat(defaultHttpRequestMaker.makeRequest(URL, STRING_TYPE))
                .isCompletedWithValue(BODY_STRING);
    }

    @Test
    public void makeRawHttpRequest() throws IOException {
        Request request = newRequest(URL);
        ResponseBody responseBody = newResponseBody(BODY_STRING);
        Response response = newResponse(request, responseBody);

        Mockito.when(httpRequestCaller.callRequest(any(Request.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        assertThat(defaultHttpRequestMaker.makeRequest(URL))
                .isCompletedWithValue(response);
    }

}
