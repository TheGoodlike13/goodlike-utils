package eu.goodlike.libraries.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * Makes HTTP GET requests using OkHttp
 *
 * Classes extending this interface can have some kind of context and may prepare the URL in some way or another
 * before making the actual request, i.e. adding additional query parameters or headers
 * </pre>
 */
public interface HttpRequestMaker {

    /**
     * Use this method when return type does not have generic parameters
     * @return future HTTP JSON response, parsed into given return class
     * @throws NullPointerException if url or returnClass is null
     */
    <T> CompletableFuture<T> makeRequest(HttpUrl url, Class<T> returnClass);

    /**
     * Use this method when return type has generic parameters
     * @return future HTTP JSON response, parsed into given return type
     * @throws NullPointerException if url or returnType is null
     */
    <T> CompletableFuture<T> makeRequest(HttpUrl url, TypeReference<T> returnType);

    /**
     * Use this method when response is not necessarily parsable using Jackson
     * @return future HTTP JSON response
     * @throws NullPointerException if request is null
     */
    CompletableFuture<Response> makeRequest(HttpUrl url);

}
