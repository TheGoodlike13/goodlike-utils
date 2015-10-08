package eu.goodlike.resty.http;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.goodlike.io.StreamReader;
import eu.goodlike.libraries.jackson.Json;
import eu.goodlike.neat.Null;
import eu.goodlike.neat.string.Str;
import eu.goodlike.resty.misc.Header;
import org.jooq.lambda.Unchecked;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * <pre>
 * This class represents an HTTP response, a result of an HTTP request
 *
 * It consists of:
 *      1) status code - defines whether the code was successful, and if not, a generic reason
 *      2) response - the body of the response, usually some kind of JSON (or error message)
 *      3) headers - response headers, with various meta-data
 *
 * The response extraction methods - rawValue(), value(Class) and value(TypeReference) - return an Optional of the
 * appropriate result; in general, prefer:
 *      a) rawValue() when dealing with errors or custom (non-JSON, or not defined) response formats
 *      b) value(Class) when dealing with non generic classes
 *      c) value(TypeReference) when dealing with generic classes
 * Also, nullable versions of these methods have been provided (they have 'Nullable' suffix in the name)
 *
 * Objects of this class are immutable
 * </pre>
 */
public final class HttpResponse {

    /**
     * @return true if the response was successful (returned 2xx), false otherwise
     */
    public boolean isSuccessful() {
        return isSuccessful(statusCode);
    }

    /**
     * @return the status code of the response
     */
    public int statusCode() {
        return statusCode;
    }

    /**
     * This method should be used if JSON parsing is not desired, or custom parsing is preferred
     * @return Optional of raw response string; empty if the response was null
     */
    public Optional<String> rawValue() {
        return Optional.ofNullable(response);
    }

    /**
     * This method should be used if JSON parsing is not desired, or custom parsing is preferred
     * @return raw response string, can be null
     */
    public String rawValueNullable() {
        return response;
    }

    /**
     * This method should be used when the resulting class IS NOT generic
     * @return Optional of given class from JSON parsed from response; empty if the response was null
     */
    public <T> Optional<T> value(Class<T> clazz) throws IOException {
        return Optional.ofNullable(response)
                    .map(Unchecked.function(r -> valueNullable(clazz)));
    }

    /**
     * This method should be used when the resulting class IS NOT generic
     * @return given class from JSON parsed from response, can be null
     */
    public <T> T valueNullable(Class<T> clazz) throws IOException {
        return response == null ? null : Json.from(response).to(clazz);
    }

    /**
     * This method should be used when the resulting class IS generic
     * @return Optional of given type from JSON parsed from response; empty if the response was null
     */
    public <T> Optional<T> value(TypeReference<T> type) throws IOException {
        return Optional.ofNullable(response)
                .map(Unchecked.function(r -> valueNullable(type)));
    }

    /**
     * This method should be used when the resulting class IS generic
     * @return given type from JSON parsed from response, can be null
     */
    public <T> T valueNullable(TypeReference<T> type) throws IOException {
        return response == null ? null : Json.from(response).to(type);
    }

    /**
     * @return set of headers that arrived with the response
     */
    public Set<Header> headerSet() {
        return this.headers.entrySet().stream()
                .map(header -> new Header(header.getKey(), header.getValue()))
                .collect(toSet());
    }

    /**
     * @return headers that arrived with the response in "HeaderName" -> "HeaderValue" Map format
     */
    public Map<String, String> headerMap() {
        return new HashMap<>(headers);
    }

    /**
     * @return Optional of header with a particular name; empty if such a header was not found in the response
     */
    public Optional<Header> headerForName(String name) {
        return headerValue(name).map(value -> new Header(name, value));
    }

    /**
     * @return header with a particular name; null if such a header was not found in the response
     */
    public Header headerForNameNullable(String name) {
        String value = headerValueNullable(name);
        return value == null ? null : new Header(name, value);
    }

    /**
     * @return Optional of header value from header with a particular name; empty if such a header was not found in the response
     */
    public Optional<String> headerValue(String name) {
        return Optional.ofNullable(headerValueNullable(name));
    }

    /**
     * @return header value from header with a particular name; null if such a header was not found in the response
     */
    public String headerValueNullable(String name) {
        return headers.get(name);
    }

    /**
     * @return true if the given status code is considered successful (2xx), false otherwise
     */
    public static boolean isSuccessful(int statusCode) {
        return statusCode / 100 == 2;
    }

    // CONSTRUCTORS

    /**
     * <pre>
     * If the request was successful (statusCode = 2xx), then the body of response will be read directly
     *
     * If it was not successful, and error message will be parsed instead, and response message will be put into headers
     * if needed
     *
     * Once the message has been read, the reader closes the appropriate stream
     * </pre>
     * @return HttpResponse from a given HttpURLConnection; usually used by HttpRequest of MultipartRequest
     * @throws NullPointerException if request is null
     * @throws IOException if the response couldn't be retrieved
     */
    public static HttpResponse from(HttpURLConnection request) throws IOException {
        Null.check(request).ifAny("Request cannot be null");

        int statusCode = request.getResponseCode();
        Map<String, String> responseHeaders = request.getHeaderFields().keySet().stream()
                .filter(headerName -> headerName != null)
                .collect(toMap(headerName -> headerName, request::getHeaderField));

        StreamReader reader;
        String message;
        if (HttpResponse.isSuccessful(statusCode)) {
            reader = StreamReader.forInputStream(request.getInputStream());
            message = reader.read();
        } else {
            reader = StreamReader.forInputStream(request.getErrorStream());
            message = reader.read();
            if (message.isEmpty())
                message = request.getResponseMessage();
            else
                responseHeaders.put("Message", request.getResponseMessage());
        }
        reader.close();
        return new HttpResponse(statusCode, message, responseHeaders);
    }

    /**
     * Private constructor; only instances created using from() with HttpURLConnection are allowed
     */
    private HttpResponse(int statusCode, String response, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.response = response;
        this.headers = headers;
    }

    // PRIVATE

    private final int statusCode;
    private final String response;
    private final Map<String, String> headers;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return Str.of("HTTP ", statusCode, "\n")
                .andSome(headerSet(), '\n')
                .and("\n", response)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpResponse)) return false;
        HttpResponse that = (HttpResponse) o;
        return Objects.equals(statusCode, that.statusCode) &&
                Objects.equals(response, that.response) &&
                Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, response, headers);
    }

}
