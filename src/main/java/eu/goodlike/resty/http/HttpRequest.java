package eu.goodlike.resty.http;

import eu.goodlike.libraries.jackson.Json;
import eu.goodlike.neat.Null;
import eu.goodlike.neat.string.Str;
import eu.goodlike.resty.http.steps.*;
import eu.goodlike.resty.misc.Header;
import eu.goodlike.resty.misc.Param;
import eu.goodlike.retry.Retry;
import eu.goodlike.retry.steps.TimesStep;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static eu.goodlike.resty.http.HttpMethod.GET;
import static java.util.stream.Collectors.joining;

/**
 * <pre>
 * This class represents an HTTP request
 *
 * It consists of:
 *      1) URL string - where the request is sent
 *      2) HTTP method - GET, POST, PUT, DELETE
 *      3) headers (optional) - set of headers sent with the request, containing meta-data
 *      4) body (optional) - usually some kind of JSON or other supported formats
 *
 * Supported body formats:
 *      application/json
 *      multipart/form-data
 *      x-www-form-urlencoded
 *
 * Objects of this class are mutable and intended to be used as a builder, which culminates in an HttpResponse or similar
 * </pre>
 */
public final class HttpRequest implements BodyTypeStep, HeaderStep, JsonBodyStep, MultipartBodyStep, QueryParamStep, ResultStep {

    @Override
    public HttpRequest noBody() {
        return form();
    }

    @Override
    public HttpRequest JSON() {
        this.isMultipart = false;
        this.boundary = null;
        this.multipartParams = null;
        this.multipartFiles = null;
        this.headers.put("Content-Type", "application/json;charset=UTF-8");
        return this;
    }

    @Override
    public HttpRequest multipart() {
        this.isMultipart = true;
        this.boundary = "===" + System.currentTimeMillis() + "===";
        this.multipartParams = new HashSet<>();
        this.multipartFiles = new HashMap<>();
        this.headers.put("Content-Type", "multipart/form-data; boundary=" + boundary);
        return this;
    }

    @Override
    public HttpRequest form() {
        this.isMultipart = false;
        this.boundary = null;
        this.multipartParams = null;
        this.multipartFiles = null;
        this.headers.put("Content-Type", "application/x-www-form-urlencoded");
        return this;
    }

    @Override
    public HttpRequest header(String name, String value) {
        return headers(Header.of(name, value));
    }

    @Override
    public HttpRequest headers(Header... headers) {
        Null.checkArray(headers).ifAny("Header array cannot be or contain null");
        for (Header header : headers)
            this.headers.put(header.name(), header.value());
        return this;
    }

    @Override
    public HttpRequest headers(Collection<Header> headers) {
        Null.checkCollection(headers).ifAny("Headers cannot be or contain null");
        for (Header header : headers)
            this.headers.put(header.name(), header.value());
        return this;
    }

    @Override
    public HttpRequest raw(String string) {
        Null.check(string).ifAny("JSON string cannot be null");
        this.simpleBody = string;
        return this;
    }

    @Override
    public HttpRequest parse(Object jsonObject) throws IOException {
        this.simpleBody = Json.stringFrom(jsonObject);
        return this;
    }

    @Override
    public HttpRequest field(String name, Object value) {
        return fields(Param.of(name, value));
    }

    @Override
    public HttpRequest fields(Param... params) {
        Null.checkArray(params).ifAny("Param array cannot be or contain null");
        this.simpleBody = null;
        Collections.addAll(this.multipartParams, params);
        return this;
    }

    @Override
    public HttpRequest fields(Collection<Param> params) {
        Null.checkCollection(params).ifAny("Params cannot be or contain null");
        this.simpleBody = null;
        this.multipartParams.addAll(params);
        return this;
    }

    @Override
    public HttpRequest file(String fieldName, File uploadedFile) {
        Null.check(fieldName, uploadedFile).ifAny("Field name and file cannot be null");
        this.simpleBody = null;
        this.multipartFiles.put(fieldName, uploadedFile);
        return this;
    }

    @Override
    public HttpRequest param(String name, Object value) {
        return params(Param.of(name, value));
    }

    @Override
    public HttpRequest params(Param... params) {
        Null.checkAlone(params).ifAny("Param array cannot be null");
        return params(Arrays.asList(params));
    }

    @Override
    public HttpRequest params(Collection<Param> params) {
        Null.checkCollection(params).ifAny("Params cannot be or contain null");
        String queryString = params.stream().filter(Param::hasValue).map(Param::toString).collect(joining("&"));
        if (httpMethod == GET)
            url += (url.contains("?") ? "&" : "?") + queryString;
        else
            this.simpleBody = this.simpleBody == null ? queryString : this.simpleBody + "&" + queryString;
        return this;
    }

    @Override
    public HttpResponse perform() throws IOException {
        return isMultipart
                ? new MultipartRequest(httpMethod, url, headers, boundary, multipartParams, multipartFiles).perform()
                : performSimple();
    }

    @Override
    public Callable<HttpResponse> performLater() {
        return this::perform;
    }

    @Override
    public Future<HttpResponse> performAsync() {
        return Executors.newSingleThreadExecutor().submit(performLater());
    }

    @Override
    public TimesStep<HttpResponse> retry() {
        return Retry.This(performLater());
    }

    // CONSTRUCTORS

    public HttpRequest(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.headers = new HashMap<>();
        this.isMultipart = false;
    }

    // PRIVATE

    private HttpMethod httpMethod;
    private String url;
    private Map<String, String> headers;
    private String simpleBody;

    private boolean isMultipart;
    private String boundary;
    private Set<Param> multipartParams;
    private Map<String, File> multipartFiles;

    private HttpResponse performSimple() throws IOException {
        HttpURLConnection request = (HttpURLConnection) (new URL(url).openConnection());

        request.setRequestMethod(httpMethod.name());
        if (httpMethod != GET && simpleBody != null)
            request.setDoOutput(true);

        headers.entrySet().forEach(header -> request.setRequestProperty(header.getKey(), header.getValue()));

        request.connect();
        if (httpMethod != GET && simpleBody != null) {
            OutputStreamWriter writer = new OutputStreamWriter(request.getOutputStream());
            writer.write(simpleBody);
            writer.flush();
        }
        return HttpResponse.from(request);
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return Str.of(httpMethod, " ", url, "\n")
                .andSome(headers.entrySet(),
                        (builder, header) -> builder.append(header.getKey()).append(": ").append(header.getValue()).append("\n"))
                .andSomeIf(isMultipart, "\n", multipartParams)
                .andSomeIf(isMultipart, "\n", multipartFiles == null ? null : multipartFiles.keySet())
                .andIf(!isMultipart, "\n", simpleBody)
                .toString();
    }

}
