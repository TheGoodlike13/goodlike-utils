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
 * Makes HTTP GET requests using OkHttp
 *
 * This is a default implementation which does not modify the url in any way
 * </pre>
 */
public final class DefaultHttpRequestMaker implements HttpRequestMaker {

    @Override
    public <T> CompletableFuture<T> makeRequest(HttpUrl url, Class<T> returnClass) {
        Null.check(url, returnClass).as("url, returnClass");
        return httpRequestCaller.callRequest(prepareRequest(url), returnClass);
    }

    @Override
    public <T> CompletableFuture<T> makeRequest(HttpUrl url, TypeReference<T> returnType) {
        Null.check(url, returnType).as("url, returnType");
        return httpRequestCaller.callRequest(prepareRequest(url), returnType);
    }

    @Override
    public CompletableFuture<Response> makeRequest(HttpUrl url) {
        Null.check(url).as("url");
        return httpRequestCaller.callRequest(prepareRequest(url));
    }

    // CONSTRUCTORS

    public DefaultHttpRequestMaker(HttpRequestCaller httpRequestCaller) {
        this.httpRequestCaller = httpRequestCaller;
    }

    // PRIVATE

    private final HttpRequestCaller httpRequestCaller;

    private Request prepareRequest(HttpUrl url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    private Request preparePostRequest(HttpUrl url, RequestBody body) {
        return new Request.Builder()
                .post(body)
                .url(url)
                .build();
    }

}
