package eu.goodlike.libraries.okhttp;

import eu.goodlike.neat.Null;
import okhttp3.OkHttpClient;

/**
 * Wraps given OkHttpClient in an AutoCloseable, so that it can be closed easily (i.e. when async callback executor
 * was created)
 */
public final class ClientWrapper implements AutoCloseable {

    public OkHttpClient getClient() {
        return client;
    }

    @Override
    public void close() throws Exception {
        client.dispatcher().executorService().shutdown();
    }

    // CONSTRUCTORS

    public ClientWrapper(OkHttpClient client) {
        Null.check(client).ifAny("Client cannot be null");

        this.client = client;
    }

    // PRIVATE

    private final OkHttpClient client;

}
