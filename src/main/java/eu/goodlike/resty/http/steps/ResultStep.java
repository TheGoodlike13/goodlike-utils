package eu.goodlike.resty.http.steps;

import eu.goodlike.resty.http.HttpResponse;
import eu.goodlike.retry.steps.TimesStep;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Final step of HTTP request builder, defines how the request should be consumed
 */
public interface ResultStep {

    /**
     * @return HttpResponse of the built HttpRequest, immediately
     * @throws IOException if HTTP connection or writing to it fails
     */
    HttpResponse perform() throws IOException;

    /**
     * @return Callable, equivalent to () -> perform(), to be consumed later
     */
    Callable<HttpResponse> performLater();

    /**
     * @return Future, equivalent to Executors.newSingleThreadExecutor().submit(performLater()), to be consumed later
     */
    Future<HttpResponse> performAsync();

    /**
     * @return Retry builder step, equivalent to Retry.This(performLater()), to define how this request is to be
     * retried
     */
    TimesStep<HttpResponse> retry();

}
