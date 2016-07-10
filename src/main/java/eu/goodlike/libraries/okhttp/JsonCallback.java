package eu.goodlike.libraries.okhttp;

import eu.goodlike.functional.IOFunction;
import eu.goodlike.neat.Null;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * <pre>
 * Callback implementation for OkHttp client
 *
 * Uses CompletableFuture and given JSON parser to handle the Response, completing exceptionally on failure or parsing
 * error
 *
 * If you use Jackson library for JSON parsing, use JacksonCallback
 * </pre>
 */
public final class JsonCallback<T> implements Callback {

    @Override
    public void onFailure(Call call, IOException e) {
        future.completeExceptionally(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        T result;
        try {
            result = jsonParser.apply(response);
        } catch (IOException e) {
            future.completeExceptionally(e);
            return;
        }

        future.complete(result);
    }

    /**
     * <pre>
     * Constructs a new Callback for the given call, which completes the returned CompletableFuture, exceptionally
     * on failure of any kind
     * </pre>
     * @return CompletableFuture which contains the response body of this call, parsed using given jsonParser
     * @throws NullPointerException if call is null
     */
    public static <T> CompletableFuture<T> asFuture(Call call, IOFunction<Response, T> jsonParser) {
        Null.check(call, jsonParser).ifAny("Call and class cannot be null");
        return new JsonCallback<>(jsonParser).enqueueFor(call);
    }

    // CONSTRUCTORS

    // used by JacksonCallback
    JsonCallback(IOFunction<Response, T> jsonParser) {
        this.future = new CompletableFuture<>();
        this.jsonParser = jsonParser;
    }

    // PACKAGE PRIVATE

    // used by JacksonCallback
    CompletableFuture<T> enqueueFor(Call call) {
        call.enqueue(this);
        return future;
    }

    // PRIVATE

    private final CompletableFuture<T> future;
    private final IOFunction<Response, T> jsonParser;

}
