package eu.goodlike.libraries.okhttp;

import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility methods to help create/transform HttpUrls
 */
public final class HttpUrls {

    /**
     * @return HttpUrl parsed from given url, empty if it would have returned null
     * @throws NullPointerException if url is null
     */
    public static Optional<HttpUrl> parse(String url) {
        Null.check(url).ifAny("Url cannot be null");
        return Optional.ofNullable(HttpUrl.parse(url));
    }

    /**
     * @return HttpUrl parsed from given url, empty if it would have returned null
     * @throws NullPointerException if url is null
     */
    public static Optional<HttpUrl> parse(URL url) {
        Null.check(url).ifAny("Url cannot be null");
        return Optional.ofNullable(HttpUrl.get(url));
    }

    /**
     * @return HttpUrl parsed from given url, empty if it would have returned null
     * @throws NullPointerException if url is null
     */
    public static Optional<HttpUrl> parse(URI url) {
        Null.check(url).ifAny("Url cannot be null");
        return Optional.ofNullable(HttpUrl.get(url));
    }

    /**
     * <pre>
     * A path variable is a path part of the URL which starts with an ":"
     *
     * This variable is intended to be replaced with some sort of value before the URL can be used
     *
     * This method assumes the path variables are already encoded so that they are path of a valid URL
     *
     * Map keys don't need the ":" prefix to be matched
     *
     * Keys that do not match any path variables in the httpUrl will be ignored
     * </pre>
     * @return httpUrl with all path variables in it replaced with values from given map
     * @throws NullPointerException if httpUrl or pathVariables is null
     * @throws IllegalArgumentException if pathVariables does not contain all of the path variables in the httpUrl
     */
    public static HttpUrl insertEncodedPathVariables(HttpUrl httpUrl, Map<String, String> pathVariables) {
        return insertPathVariables(httpUrl, pathVariables, AlreadyEncoded.YES);
    }

    /**
     * <pre>
     * A path variable is a path part of the URL which starts with an ":"
     *
     * This variable is intended to be replaced with some sort of value before the URL can be used
     *
     * This method encodes the path variable values so that they are path of a valid URL
     *
     * Map keys don't need the ":" prefix to be matched
     *
     * Keys that do not match any path variables in the httpUrl will be ignored
     * </pre>
     * @return httpUrl with all path variables in it replaced with values from given map
     * @throws NullPointerException if httpUrl or pathVariables is null
     * @throws IllegalArgumentException if pathVariables does not contain all of the path variables in the httpUrl
     */
    public static HttpUrl insertPathVariables(HttpUrl httpUrl, Map<String, String> pathVariables) {
        return insertPathVariables(httpUrl, pathVariables, AlreadyEncoded.NO);
    }

    /**
     * @return true if given path part starts with ":"
     * @throws NullPointerException if pathPart is null
     */
    public static boolean isPathVariable(String pathPart) {
        Null.check(pathPart).ifAny("Path part cannot be null");
        return isPathVariableNoNull(pathPart);
    }

    // PRIVATE

    private HttpUrls() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

    private static HttpUrl insertPathVariables(HttpUrl httpUrl, Map<String, String> pathVariables, AlreadyEncoded isAlreadyEncoded) {
        Null.check(httpUrl, pathVariables).ifAny("HttpUrl and pathVariables cannot be null");

        List<String> path = httpUrl.pathSegments();
        if (path.isEmpty())
            return httpUrl;

        if (pathVariables.isEmpty())
            throw new IllegalArgumentException("No values given, yet there are path variables: " + path.stream()
                    .filter(HttpUrls::isPathVariableNoNull)
                    .collect(Collectors.joining(", ", "{", "}")));

        HttpUrl.Builder builder = httpUrl.newBuilder();
        for (int i = 0; i < path.size(); i++) {
            String pathPart = path.get(i);
            if (isPathVariableNoNull(pathPart)) {
                String value = pathVariables.get(pathPart);
                if (value == null)
                    value = pathVariables.get(pathPart.substring(1));

                if (value == null)
                    throw new IllegalArgumentException("No value given for path variable " + pathPart);

                if (isAlreadyEncoded.isTrue())
                    builder.setEncodedPathSegment(i, value);
                else
                    builder.setPathSegment(i, value);
            }
        }

        return builder.build();
    }

    private static boolean isPathVariableNoNull(String pathPart) {
        return pathPart.startsWith(":");
    }

    private enum AlreadyEncoded {
        YES, NO;

        public boolean isTrue() {
            return this == YES;
        }

        public boolean isFalse() {
            return this == NO;
        }
    }

}
