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

public class DefaultHttpPostRequestMakerTest {

    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");
    private static final String BODY_STRING = "body_string";
    private static final TypeReference<String> STRING_TYPE = new TypeReference<String>() {};

    private HttpRequestCaller requestCaller;
    private HttpPostRequestMaker postRequestMaker;

    @Before
    public void setUp() {
        requestCaller = Mockito.mock(HttpRequestCaller.class);
        postRequestMaker = new DefaultHttpPostRequestMaker(requestCaller);
    }

    private RequestBody newRequestBody(Object requestBody) throws IOException {
        return RequestBody.create(MediaType.parse("application/json"), Json.stringFrom(requestBody));
    }

    private Request newPostRequest(HttpUrl url, RequestBody requestBody) {
        return new Request.Builder().post(requestBody).url(url).build();
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
                .build();
    }

    @Test
    public void makePostRequestForGivenClass() throws IOException {
        RequestBody requestBody = newRequestBody(BODY_STRING);

        Mockito.when(requestCaller.callRequest(any(Request.class), eq(String.class)))
                .thenReturn(CompletableFuture.completedFuture(BODY_STRING));

        assertThat(postRequestMaker.postRequest(URL, requestBody, String.class))
                .isCompletedWithValue(BODY_STRING);
    }

    @Test
    public void makePostRequestForGivenType() throws IOException {
        RequestBody requestBody = newRequestBody(BODY_STRING);

        Mockito.when(requestCaller.callRequest(any(Request.class), eq(STRING_TYPE)))
                .thenReturn(CompletableFuture.completedFuture(BODY_STRING));

        assertThat(postRequestMaker.postRequest(URL, requestBody, STRING_TYPE))
                .isCompletedWithValue(BODY_STRING);
    }

    @Test
    public void makeRawPostRequest() throws IOException {
        RequestBody requestBody = newRequestBody(BODY_STRING);
        Request request = newPostRequest(URL, requestBody);
        ResponseBody responseBody = newResponseBody(BODY_STRING);
        Response response = newResponse(request, responseBody);

        Mockito.when(requestCaller.callRequest(any(Request.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        Response actualResponse = postRequestMaker.postRequest(URL, requestBody).join();
        String actualBody = actualResponse.body().string();
        assertThat(Json.read(String.class).from(actualBody))
                .isEqualTo(BODY_STRING);
    }

}
