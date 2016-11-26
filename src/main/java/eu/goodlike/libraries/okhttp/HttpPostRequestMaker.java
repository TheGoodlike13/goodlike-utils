package eu.goodlike.libraries.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * Makes HTTP POST requests using OkHttp
 *
 * Classes extending this interface can have some kind of context and may prepare the URL in some way or another
 * before making the actual request, i.e. adding additional query parameters or headers
 * </pre>
 */
public interface HttpPostRequestMaker {
    /**
     * Use this method when return type does not have generic parameters
     * @return future HTTP JSON response, parsed into given return class
     * @throws NullPointerException if url, body or returnClass is null
     */
    <T> CompletableFuture<T> postRequest(HttpUrl url, RequestBody body, Class<T> returnClass);

    /**
     * Use this method when return type has generic parameters
     * @return future HTTP JSON response, parsed into given return type
     * @throws NullPointerException if url, body or returnType is null
     */
    <T> CompletableFuture<T> postRequest(HttpUrl url, RequestBody body, TypeReference<T> returnType);

    /**
     * Use this method when response is not necessarily parsable using Jackson
     * @return future HTTP JSON response
     * @throws NullPointerException if request or body is null
     */
    CompletableFuture<Response> postRequest(HttpUrl url, RequestBody body);
}
