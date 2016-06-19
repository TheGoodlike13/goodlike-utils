package eu.goodlike.libraries.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.goodlike.libraries.jackson.Json;
import eu.goodlike.neat.Null;
import okhttp3.Call;

import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * Static factory methods for callback implementation for okhttp client
 *
 * Uses CompletableFuture and Jackson to handle the Response, completing exceptionally on failure or parsing error
 * </pre>
 */
public final class JacksonCallback {

    /**
     * <pre>
     * Constructs a new Callback for the given call, which completes the returned CompletableFuture, exceptionally
     * on failure of any kind
     * </pre>
     * @return CompletableFuture which contains the response body of this call, parsed using Jackson and given class
     * @throws NullPointerException if call is null
     */
    public static <T> CompletableFuture<T> asFuture(Call call, Class<T> clazz) {
        Null.check(call, clazz).ifAny("Call and class cannot be null");
        return new JsonCallback<>(response -> Json.from(response.body().byteStream()).to(clazz)).enqueueFor(call);
    }

    /**
     * <pre>
     * Constructs a new Callback for the given call, which completes the returned CompletableFuture, exceptionally
     * on failure of any kind
     * </pre>
     * @return CompletableFuture which contains the response body of this call, parsed using Jackson and given type
     * @throws NullPointerException if call is null
     */
    public static <T> CompletableFuture<T> asFuture(Call call, TypeReference<T> typeReference) {
        Null.check(call, typeReference).ifAny("Call and class cannot be null");
        return new JsonCallback<>(response -> Json.from(response.body().byteStream()).to(typeReference)).enqueueFor(call);
    }

    // PRIVATE

    private JacksonCallback() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
