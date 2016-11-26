package eu.goodlike.libraries.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * Makes HTTP POST requests using OkHttp
 *
 * This is a default implementation which does not modify the url in any way
 * </pre>
 */
public final class DefaultHttpPostRequestMaker implements HttpPostRequestMaker {

    @Override
    public <T> CompletableFuture<T> postRequest(HttpUrl url, RequestBody body, Class<T> returnClass) {
        Null.check(url, body, returnClass).as("url, body, returnClass");
        return httpRequestCaller.callRequest(preparePostRequest(url, body), returnClass);
    }

    @Override
    public <T> CompletableFuture<T> postRequest(HttpUrl url, RequestBody body, TypeReference<T> returnType) {
        Null.check(url, body, returnType).as("url, body, returnType");
        return httpRequestCaller.callRequest(preparePostRequest(url, body), returnType);
    }

    @Override
    public CompletableFuture<Response> postRequest(HttpUrl url, RequestBody body) {
        Null.check(url, body).as("url, body");
        return httpRequestCaller.callRequest(preparePostRequest(url, body));
    }

    // CONSTRUCTORS

    public DefaultHttpPostRequestMaker(HttpRequestCaller httpRequestCaller) {
        this.httpRequestCaller = httpRequestCaller;
    }

    // PRIVATE

    private final HttpRequestCaller httpRequestCaller;

    private Request preparePostRequest(HttpUrl url, RequestBody body) {
        return new Request.Builder()
                .post(body)
                .url(url)
                .build();
    }

}
