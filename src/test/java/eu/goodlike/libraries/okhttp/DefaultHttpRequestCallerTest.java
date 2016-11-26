package eu.goodlike.libraries.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

public class DefaultHttpRequestCallerTest {

    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");
    private static final String BODY_STRING = "body_string";
    private static final TypeReference<String> STRING_TYPE = new TypeReference<String>() {};

    private OkHttpClient okHttpClient;
    private HttpRequestCaller defaultHttpRequestCaller;

    @Before
    public void setup() throws Exception {
        okHttpClient = Mockito.mock(OkHttpClient.class);
        defaultHttpRequestCaller = new DefaultHttpRequestCaller(okHttpClient);
    }

    private Request newGetRequest(HttpUrl url) {
        return new Request.Builder().url(url).build();
    }

    @Test
    public void callHttpRequestForGivenClass() throws IOException {
        Request request = newGetRequest(URL);
        Call result = new CallMock(request, BODY_STRING);

        Mockito.when(okHttpClient.newCall(any(Request.class)))
                .thenReturn(result);

        assertThat(defaultHttpRequestCaller.callRequest(request, String.class))
                .isCompletedWithValue(BODY_STRING);
    }

    @Test
    public void callHttpRequestForGivenType() throws IOException {
        Request request = newGetRequest(URL);
        Call result = new CallMock(request, BODY_STRING);

        Mockito.when(okHttpClient.newCall(any(Request.class)))
                .thenReturn(result);

        assertThat(defaultHttpRequestCaller.callRequest(request, STRING_TYPE))
                .isCompletedWithValue(BODY_STRING);
    }

    @Test
    public void callHttpRequestForRawResponse() throws IOException {
        Request request = newGetRequest(URL);
        Call result = new CallMock(request, BODY_STRING);

        Mockito.when(okHttpClient.newCall(any(Request.class)))
                .thenReturn(result);

        assertThat(defaultHttpRequestCaller.callRequest(request))
                .isCompletedWithValue(result.execute());
    }

}
