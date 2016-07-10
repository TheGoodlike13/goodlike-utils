package eu.goodlike.libraries.okhttp;

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
 * Uses CompletableFuture to handle the Response, completing exceptionally on failure
 *
 * If you would like to have the response automatically parsed as JSON, use JsonCallback
 *
 * If you use Jackson library for JSON parsing, use JacksonCallback
 * </pre>
 */
public final class ResponseCallback implements Callback {

    @Override
    public void onFailure(Call call, IOException e) {
        future.completeExceptionally(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        future.complete(response);
    }

    /**
     * <pre>
     * Constructs a new Callback for the given call, which completes the returned CompletableFuture, exceptionally
     * on failure of any kind
     * </pre>
     * @return CompletableFuture which contains the response of this call
     * @throws NullPointerException if call is null
     */
    public static CompletableFuture<Response> asFuture(Call call) {
        Null.check(call).ifAny("Call cannot be null");
        return new ResponseCallback().enqueueFor(call);
    }

    // CONSTRUCTORS

    private ResponseCallback() {
        this.future = new CompletableFuture<>();
    }

    // PRIVATE

    private final CompletableFuture<Response> future;

    private CompletableFuture<Response> enqueueFor(Call call) {
        call.enqueue(this);
        return future;
    }

}
