package eu.goodlike.libraries.okhttp;

import eu.goodlike.libraries.jackson.Json;
import okhttp3.*;

import java.io.IOException;

/**
 * Very basic mock for {@link Call}
 */
public final class CallMock implements Call {

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response execute() throws IOException {
        return response;
    }

    @Override
    public void enqueue(Callback responseCallback) {
        try {
            responseCallback.onResponse(this, execute());
        } catch (IOException e) {
            throw new AssertionError("IOException cannot occur when response is an in-memory String");
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isExecuted() {
        return true;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call clone() {
        return this;
    }

    // CONSTRUCTORS

    CallMock(Request request, Object jsonValue) throws IOException {
        this.request = request;
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), Json.stringFrom(jsonValue));
        this.response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .body(responseBody)
                .build();
    }

    // PRIVATE

    private final Request request;
    private final Response response;

}
