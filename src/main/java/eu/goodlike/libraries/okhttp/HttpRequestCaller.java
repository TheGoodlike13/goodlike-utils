package eu.goodlike.libraries.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * Executes HTTP requests using OkHttp
 *
 * Classes extending this interface can have some kind of context and may prepare the request in some way or another
 * before making the actual request, i.e. adding additional query parameters
 * </pre>
 */
public interface HttpRequestCaller {

    /**
     * Use this method when return type does not have generic parameters
     * @return future HTTP JSON response, parsed into given return class
     * @throws NullPointerException if request or returnClass is null
     */
    <T> CompletableFuture<T> callRequest(Request request, Class<T> returnClass);

    /**
     * Use this method when return type has generic parameters
     * @return future HTTP JSON response, parsed into given return type
     * @throws NullPointerException if request or returnType is null
     */
    <T> CompletableFuture<T> callRequest(Request request, TypeReference<T> returnType);

    /**
     * Use this method when response is not necessarily parsable using Jackson
     * @return future HTTP JSON response
     * @throws NullPointerException if request is null
     */
    CompletableFuture<Response> callRequest(Request request);

}
