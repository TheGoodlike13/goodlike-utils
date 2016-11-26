package eu.goodlike.libraries.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.goodlike.neat.Null;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * Executes HTTP requests using OkHttp
 *
 * This is a default implementation which does not modify the request in any way
 * </pre>
 */
public final class DefaultHttpRequestCaller implements HttpRequestCaller {

    @Override
    public <T> CompletableFuture<T> callRequest(Request request, Class<T> returnClass) {
        Null.check(request, returnClass).as("request, returnClass");
        return JacksonCallback.asFuture(sendCallToClient(request), returnClass);
    }

    @Override
    public <T> CompletableFuture<T> callRequest(Request request, TypeReference<T> returnType) {
        Null.check(request, returnType).as("request, returnType");
        return JacksonCallback.asFuture(sendCallToClient(request), returnType);
    }

    @Override
    public CompletableFuture<Response> callRequest(Request request) {
        Null.check(request).as("request");
        return ResponseCallback.asFuture(sendCallToClient(request));
    }

    // CONSTRUCTORS

    public DefaultHttpRequestCaller(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    // PRIVATE

    private final OkHttpClient okHttpClient;

    private Call sendCallToClient(Request request) {
        return okHttpClient.newCall(request);
    }

}
