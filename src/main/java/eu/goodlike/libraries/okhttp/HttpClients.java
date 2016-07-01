package eu.goodlike.libraries.okhttp;

import eu.goodlike.neat.Null;
import okhttp3.OkHttpClient;

/**
 * Utility methods to help with OkHttpClient
 */
public final class HttpClients {

    /**
     * @return new default OkHttpClient
     */
    public static OkHttpClient newInstance() {
        return new OkHttpClient();
    }

    /**
     * Closes given OkHttpClient
     * @throws NullPointerException if okHttpClient is null
     */
    public static void close(OkHttpClient okHttpClient) {
        Null.check(okHttpClient).ifAny("Http client cannot be null");
        okHttpClient.dispatcher().executorService().shutdown();
    }

    // PRIVATE

    private HttpClients() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
