package eu.goodlike.resty.url;

import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.neat.Null;
import eu.goodlike.neat.string.Str;
import eu.goodlike.resty.http.HttpMethod;
import eu.goodlike.resty.http.HttpRequest;
import eu.goodlike.resty.http.steps.BodyTypeStep;
import eu.goodlike.resty.http.steps.QueryParamStep;
import eu.goodlike.resty.http.steps.custom.CustomDynamicStepBuilder;
import eu.goodlike.resty.misc.PathVar;

import java.util.*;
import java.util.stream.Stream;

import static eu.goodlike.misc.Constants.NOT_NULL_NOT_BLANK;
import static eu.goodlike.resty.http.HttpMethod.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * <pre>
 * This class represents an adjustable URL
 *
 * It consists of:
 *      1) protocol - http or https
 *      2) IP address - basically any non-null non-blank String is allowed
 *      3) port (optional); 1 <= port <= 65535
 *      4) path (optional, can have variables) - any strings that are separated by '/', variables start with ':'
 *
 * Parameters are added as part of HTTP request creation process
 *
 * Objects of this class are immutable
 * </pre>
 */
public final class DynamicURL {

    /**
     * @return url String made from this DynamicURL
     */
    public String getUrl() {
        return Str.of(protocol, "://", ip)
                .andIf(port != null, ":", port)
                .andSome("/", path)
                .toString();
    }

    /**
     * @return this DynamicURL with given protocol; only constructs a new one if needed
     * @throws NullPointerException if protocol is null
     */
    public DynamicURL withProtocol(Protocol protocol) {
        Null.check(protocol).ifAny("Protocol cannot be null");
        return this.protocol == protocol ? this : new DynamicURL(protocol, ip, port, path);
    }

    /**
     * @return this DynamicURL with HTTP protocol; only constructs a new one if needed
     */
    public DynamicURL withHTTP() {
        return withProtocol(Protocol.http);
    }

    /**
     * @return this DynamicURL with HTTPS protocol; only constructs a new one if needed
     */
    public DynamicURL withHTTPS() {
        return withProtocol(Protocol.https);
    }

    /**
     * @return this DynamicURL with given IP; only constructs a new one if needed
     * @throws IllegalArgumentException if ip is null or blank
     */
    public DynamicURL withIP(String ip) {
        NOT_NULL_NOT_BLANK.ifInvalidThrow(ip, DynamicURL::invalidIPMessage);
        return this.ip.equals(ip) ? this : new DynamicURL(protocol, ip, port, path);
    }

    /**
     * @return this DynamicURL with given port; only constructs a new one if needed
     * @throws IllegalArgumentException if port is not between 1 and 65535, which are the only valid ports
     */
    public DynamicURL withPort(int port) {
        if (port < MIN_PORT || port > MAX_PORT)
            throw new IllegalArgumentException("Port must be between " + MIN_PORT + " and " + MAX_PORT);
        return this.port != null && this.port == port ? this : new DynamicURL(protocol, ip, port, path);
    }

    /**
     * @return this DynamicURL without any port; only constructs a new one if needed
     */
    public DynamicURL withoutPort() {
        return this.port == null ? this : new DynamicURL(protocol, ip, null, path);
    }

    /**
     * <pre>
     * The paths given to this method are parsed using the following logic:
     *      1) If path contains '//', throw an exception
     *      2) If path starts or ends with '/', trim them
     *      3) Split the string on '/' and add all resulting strings together
     *      4) If the string starts with ':', it is considered a variable
     * </pre>
     * @return this DynamicURL with given path; only constructs a new one if needed
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any path contains '//', is empty or equal to '/'
     */
    public DynamicURL withPath(String... path) {
        Null.checkAlone(path).ifAny("Path cannot be null");
        return withPath(Arrays.asList(path));
    }

    /**
     * <pre>
     * The paths given to this method are parsed using the following logic:
     *      1) If path contains '//', throw an exception
     *      2) If path starts or ends with '/', trim them
     *      3) Split the string on '/' and add all resulting strings together
     *      4) If the string starts with ':', it is considered a variable
     * </pre>
     * @return this DynamicURL with given path; only constructs a new one if needed
     * @throws NullPointerException if path list is or contains null
     * @throws IllegalArgumentException if any path contains '//', is empty or equal to '/'
     */
    public DynamicURL withPath(List<String> path) {
        Null.checkCollection(path).ifAny("Path cannot be or contain null");
        return this.path.equals(path) ? this : new DynamicURL(protocol, ip, null, parsedPath(path));
    }

    /**
     * @return this DynamicURL without any path; only constructs a new one if needed
     */
    public DynamicURL withoutPath() {
        return this.path.isEmpty() ? this : new DynamicURL(protocol, ip, port, Collections.emptyList());
    }

    /**
     * <pre>
     * The paths given to this method are parsed using the following logic:
     *      1) If path contains '//', throw an exception
     *      2) If path starts or ends with '/', trim them
     *      3) Split the string on '/' and add all resulting strings together
     *      4) If the string starts with ':', it is considered a variable
     * </pre>
     * @return this DynamicURL with path made of current path and given path put together
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any path contains '//', is empty or equal to '/'
     */
    public DynamicURL appendPath(String... path) {
        Null.checkAlone(path).ifAny("Path cannot be null");
        return appendPath(Arrays.asList(path));
    }

    /**
     * <pre>
     * The paths given to this method are parsed using the following logic:
     *      1) If path contains '//', throw an exception
     *      2) If path starts or ends with '/', trim them
     *      3) Split the string on '/' and add all resulting strings together
     *      4) If the string starts with ':', it is considered a variable
     * </pre>
     * @return this DynamicURL with path made of current path and given path put together
     * @throws NullPointerException if path list is or contains null
     * @throws IllegalArgumentException if any path contains '//', is empty or equal to '/'
     */
    public DynamicURL appendPath(List<String> path) {
        Null.checkCollection(path).ifAny("Path cannot be or contain null");
        List<String> newPath = new ArrayList<>(this.path);
        newPath.addAll(parsedPath(path));
        return new DynamicURL(protocol, ip, port, newPath);
    }

    /**
     * @return true if any of the path parts are variables, false otherwise
     */
    public boolean hasVariables() {
        return !pathVariables.isEmpty();
    }

    /**
     * The name of the variable can optionally start with ":"
     * @return true if the url contains variable with given name, false otherwise
     * @throws NullPointerException if name is null
     */
    public boolean hasVariable(String name) {
        Null.check(name).ifAny("Variable name cannot be null");
        return pathVariables.contains(name.startsWith(":") ? name : ":" + name);
    }

    /**
     * @return true if the string representing a path is variable, false otherwise
     * @throws NullPointerException if pathPart is null
     */
    public static boolean isVariable(String pathPart) {
        Null.check(pathPart).ifAny("Variable name cannot be null");
        return pathPart.startsWith(":");
    }

    /**
     * <pre>
     * Replaces the first occurring variables, left to right, with given objects
     *
     * The objects' values are evaluated using String.valueOf(), followed by an UrlEncoder.encode() using UTF-8
     *
     * If the array is empty, this DynamicURL is returned, otherwise a new one is constructed
     * </pre>
     * @return this DynamicUrl with some of its variables replaced by given objects
     * @throws NullPointerException if variable array is or contains null
     * @throws IllegalArgumentException if there are fewer path variables in the URL than values given
     * @throws IllegalArgumentException if any of the objects, when encoded return a blank String
     */
    public DynamicURL withNextValues(Object... variables) {
        Null.checkArray(variables).ifAny("Variable array cannot be or contain null");
        if (variables.length == 0)
            return this;

        if (variables.length > pathVariables.size())
            throw new IllegalArgumentException("Too many variables passed; current count: "
                    + pathVariables.size() + "; passed: " + variables.length);

        List<String> newPath = new ArrayList<>(path);
        int nextIndex = 0;
        for (Object variable : variables) {
            String value = SpecialUtils.urlEncode(variable);
            PathVar.validateValue(value);
            for (int i = nextIndex; i < newPath.size(); i++)
                if (isVariable(newPath.get(i))) {
                    newPath.set(i, value);
                    nextIndex = i + 1;
                    break;
                }
        }
        return withPath(newPath);
    }

    /**
     * <pre>
     * Replaces the first occurring variable, left to right, with given object
     *
     * The object value is evaluated using String.valueOf(), followed by an UrlEncoder.encode() using UTF-8
     * </pre>
     * @return this DynamicUrl with first of its variables replaced by given object
     * @throws NullPointerException if variable is null
     * @throws IllegalArgumentException if there are no path variables in the URL
     * @throws IllegalArgumentException if the object, when encoded returns a blank String
     */
    public DynamicURL withNextValue(Object variable) {
        Null.check(variable).ifAny("Variable value cannot be null");
        if (!hasVariables())
            throw new IllegalArgumentException("No more variables left to pass values into");

        String value = SpecialUtils.urlEncode(variable);
        PathVar.validateValue(value);

        List<String> newPath = new ArrayList<>(path);
        for (int i = 0; i < newPath.size(); i++)
            if (isVariable(newPath.get(i))) {
                newPath.set(i, value);
                break;
            }

        return withPath(newPath);
    }

    /**
     * <pre>
     * Replaces path variables with actual values
     *
     * If the array is empty, this DynamicURL is returned, otherwise a new one is constructed
     * </pre>
     * @return this DynamicUrl with some of its variables replaced using given PathVars
     * @throws NullPointerException if variable array is or contains null
     * @throws IllegalArgumentException if any of given variables does not appear in this URL
     */
    public DynamicURL withValues(PathVar... variables) {
        Null.checkArray(variables).ifAny("Variable array cannot be or contain null");
        if (variables.length == 0)
            return this;

        if (!Stream.of(variables).map(PathVar::name).allMatch(this::hasVariable))
            throw new IllegalArgumentException("Cannot find these variables in the URL: "
                    + Stream.of(variables).map(PathVar::name).filter(this::notHasVariable).collect(joining(", ")));

        List<String> newPath = new ArrayList<>(path);
        for (PathVar variable : variables)
            for (int i = 0; i < newPath.size(); i++)
                if (newPath.get(i).equals(variable.name())) {
                    newPath.set(i, variable.value());
                    break;
                }

        return withPath(newPath);
    }

    /**
     * Replaces a path variable with an actual value
     * @return this DynamicUrl with one of its variables replaced using given PathVar
     * @throws NullPointerException if variable is null
     * @throws IllegalArgumentException if given variable does not appear in this URL
     */
    public DynamicURL withValue(PathVar variable) {
        Null.check(variable).ifAny("Variable cannot be null");
        if (!hasVariables())
            throw new IllegalArgumentException("No more variables left to pass values into");

        if (!hasVariable(variable.name()))
            throw new IllegalArgumentException("Cannot find variable in the URL: " + variable.name());

        List<String> newPath = new ArrayList<>(path);
        for (int i = 0; i < newPath.size(); i++)
            if (newPath.get(i).equals(variable.name())) {
                newPath.set(i, variable.value());
                break;
            }

        return withPath(newPath);
    }

    /**
     * <pre>
     * Replaces a path variable with an actual value
     *
     * Shorthand for withValue(PathVar.of(name, value))
     * </pre>
     * @return this DynamicUrl with one of its variables replaced using given PathVar
     * @throws NullPointerException if name or value is null
     * @throws IllegalArgumentException if given name does not appear in this URL as variable
     * @throws IllegalArgumentException if the object, when encoded returns a blank String
     */
    public DynamicURL withValue(String name, Object value) {
        return withValue(PathVar.of(name, value));
    }

    /**
     * @return step builder using this DynamicURL and HTTP GET method
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public QueryParamStep GET() {
        return withGET();
    }

    /**
     * Shorthand for withPath(path).GET()
     * @return step builder using this DynamicURL with given path and HTTP GET method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public QueryParamStep GET(String... path) {
        return withGET(path);
    }

    /**
     * Shorthand for withPath(path).GET()
     * @return step builder using this DynamicURL with given path and HTTP GET method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public QueryParamStep GET(List<String> path) {
        return withGET(path);
    }

    /**
     * @return step builder using this DynamicURL and HTTP POST method
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public BodyTypeStep POST() {
        return withPOST();
    }

    /**
     * Shorthand for withPath(path).POST()
     * @return step builder using this DynamicURL with given path and HTTP POST method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public BodyTypeStep POST(String... path) {
        return withPOST(path);
    }

    /**
     * Shorthand for withPath(path).POST()
     * @return step builder using this DynamicURL with given path and HTTP POST method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public BodyTypeStep POST(List<String> path) {
        return withPOST(path);
    }

    /**
     * @return step builder using this DynamicURL and HTTP PUT method
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public BodyTypeStep PUT() {
        return withPUT();
    }

    /**
     * Shorthand for withPath(path).PUT()
     * @return step builder using this DynamicURL with given path and HTTP PUT method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public BodyTypeStep PUT(String... path) {
        return withPUT(path);
    }

    /**
     * Shorthand for withPath(path).PUT()
     * @return step builder using this DynamicURL with given path and HTTP PUT method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public BodyTypeStep PUT(List<String> path) {
        return withPUT(path);
    }

    /**
     * @return step builder using this DynamicURL and HTTP DELETE method
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public BodyTypeStep DELETE() {
        return withDELETE();
    }

    /**
     * Shorthand for withPath(path).DELETE()
     * @return step builder using this DynamicURL with given path and HTTP DELETE method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public BodyTypeStep DELETE(String... path) {
        return withDELETE(path);
    }

    /**
     * Shorthand for withPath(path).DELETE()
     * @return step builder using this DynamicURL with given path and HTTP DELETE method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public BodyTypeStep DELETE(List<String> path) {
        return withDELETE(path);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * step builder methods instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl and given HttpMethod
     * @throws NullPointerException if httpMethod is null
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public HttpRequest sending(HttpMethod httpMethod) {
        Null.check(httpMethod).ifAny("HTTP method cannot be null");
        if (hasVariables())
            throw new IllegalStateException("Cannot send while URL still has variables: " + pathVariables);
        return new HttpRequest(httpMethod, getUrl());
    }

    /**
     * <pre>
     * Shorthand for withPath(path).sending(httpMethod)
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * step builder methods instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl and given HttpMethod
     * @throws NullPointerException if httpMethod is null
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest sending(HttpMethod httpMethod, String... path) {
        return appendPath(path).sending(httpMethod);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).sending(httpMethod)
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * step builder methods instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl and given HttpMethod
     * @throws NullPointerException if httpMethod is null
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest sending(HttpMethod httpMethod, List<String> path) {
        return appendPath(path).sending(httpMethod);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * GET() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl and HTTP GET method
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public HttpRequest withGET() {
        return sending(GET);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).withGET()
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * GET() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl with given path and HTTP GET method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest withGET(String... path) {
        return sending(GET, path);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).withGET()
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * GET() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl with given path and HTTP GET method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest withGET(List<String> path) {
        return sending(GET, path);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * POST() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl and HTTP POST method
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public HttpRequest withPOST() {
        return sending(POST);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).withPOST()
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * POST() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl with given path and HTTP POST method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest withPOST(String... path) {
        return sending(POST, path);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).withPOST()
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * POST() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl with given path and HTTP POST method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest withPOST(List<String> path) {
        return sending(POST, path);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * PUT() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl and HTTP PUT method
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public HttpRequest withPUT() {
        return sending(PUT);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).withPUT()
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * PUT() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl with given path and HTTP PUT method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest withPUT(String... path) {
        return sending(PUT, path);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).withPUT()
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * PUT() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl with given path and HTTP PUT method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest withPUT(List<String> path) {
        return sending(PUT, path);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * DELETE() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl and HTTP DELETE method
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public HttpRequest withDELETE() {
        return sending(DELETE);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).withDELETE()
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * DELETE() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl with given path and HTTP DELETE method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest withDELETE(String... path) {
        return sending(DELETE, path);
    }

    /**
     * <pre>
     * Shorthand for withPath(path).withDELETE()
     *
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * DELETE() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this DynamicUrl with given path and HTTP DELETE method
     * @throws IllegalStateException if this DynamicURL or path has variables
     */
    public HttpRequest withDELETE(List<String> path) {
        return sending(DELETE, path);
    }

    /**
     * @return StaticURL version of this DynamicURL, using getUrl() method as basis
     * @throws IllegalStateException if this DynamicURL still has variables
     */
    public StaticURL toStatic() {
        if (hasVariables())
            throw new IllegalStateException("Cannot convert to static while URL still has variables: " + pathVariables);
        return StaticURL.of(this);
    }

    /**
     * <pre>
     * Custom step builder pattern enabling method
     *
     * By implementing CustomDynamicStepBuilder (or extending AbstractDynamicStepBuilder), you can create a custom
     * builder which allows to fluently construct a custom DynamicUrl, HttpRequest (or any of its steps) or even
     * get an HttpResponse
     *
     * The builder should implement FirstStep (which should be an interface) if you are going to use AbstractDynamicStepBuilder
     * </pre>
     */
    public <FirstStep> FirstStep custom(CustomDynamicStepBuilder<FirstStep> customDynamicStepBuilder) {
        return customDynamicStepBuilder.setup(this);
    }

    // CONSTRUCTORS

    /**
     * @return step builder for an DynamicURL; has two steps, one for protocol and one for IP, the rest are handled
     * directly by DynamicURL
     */
    public static ProtocolStep builder() {
        return new Builder();
    }

    /**
     * Shorthand for builder().protocol(protocol).IP(ip)
     */
    public static DynamicURL of(Protocol protocol, String ip) {
        return builder().protocol(protocol).IP(ip);
    }

    /**
     * Shorthand for builder().HTTP().IP(ip)
     */
    public static DynamicURL ofHTTP(String ip) {
        return builder().HTTP().IP(ip);
    }

    /**
     * Shorthand for builder().HTTPS().IP(ip)
     */
    public static DynamicURL ofHTTPS(String ip) {
        return builder().HTTPS().IP(ip);
    }

    /**
     * @return DynamicURL version of given StaticURL; uses the underlying url String, which is parsed using of(String)
     * @throws NullPointerException if staticURL is null
     */
    public static DynamicURL of(StaticURL staticURL) {
        Null.check(staticURL).ifAny("StaticURL cannot be null");
        return of(staticURL.getUrl());
    }

    /**
     * <pre>
     * Only certain format urls are supported:
     *      1) ALL query or fragment parts are truncated (that is, anything after the first '?' or '#')
     *      2) The url must start with "http://" or "https://"
     *      3) Everything between this start and the next '/' or end of String is considered its IP
     *      4) Everything beyond the first '/' is considered its path (normal path parsing rules apply)
     * </pre>
     * @return DynamicURL parsed from a given String
     * @throws NullPointerException if url is null
     * @throws IllegalArgumentException if url format is not supported
     */
    public static DynamicURL of(String url) {
        Null.check(url).ifAny("URL cannot be null");
        int index = url.indexOf('?');
        if (index >= 0)
            url = url.substring(0, index);
        index = url.indexOf('#');
        if (index >= 0)
            url = url.substring(0, index);

        if (url.startsWith("http"))
            url = url.substring(4);
        else
            throw new IllegalArgumentException("Invalid URL format; only http and https protocols supported");

        Protocol protocol;
        if (url.startsWith("://")) {
            protocol = Protocol.http;
            url = url.substring(3);
        } else
            if (url.startsWith("s://")) {
                protocol = Protocol.https;
                url = url.substring(4);
            } else
                throw new IllegalArgumentException("Invalid URL format; only http and https protocols supported");

        index = url.indexOf('/');
        if (index < 0)
            return of(protocol, url);

        String ip = url.substring(0, index);
        String path = url.substring(index + 1);
        DynamicURL dynamicURL = of(protocol, ip);
        return path.isEmpty() ? dynamicURL : dynamicURL.withPath(path);
    }

    /**
     * <pre>
     * Private constructor; use static method instead
     *
     * Assumes all params are validated by other means
     * </pre>
     * @throws IllegalArgumentException if any variable has no name (equal to ":") or multiple variables have same names
     */
    private DynamicURL(Protocol protocol, String ip, Integer port, List<String> path) {
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
        this.path = path;
        List<String> pathVariables = path.stream()
                .filter(DynamicURL::isVariable)
                .peek(DynamicURL::failOnEmptyVariable)
                .collect(toList());
        this.pathVariables = new HashSet<>(pathVariables);
        if (this.pathVariables.size() != pathVariables.size())
            throw new IllegalArgumentException("URL cannot have multiple variables with the same name!");
    }

    // PRIVATE

    private final Protocol protocol;
    private final String ip;
    private final Integer port;
    private final List<String> path;
    private final Set<String> pathVariables;

    /**
     * Convenience method for !hasVariable(String) in Predicate lambdas
     */
    private boolean notHasVariable(String name) {
        return !hasVariable(name);
    }

    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;

    private static final DynamicURL DEFAULT_HTTP_DYNAMIC_URL = new DynamicURL(Protocol.http, "localhost", null, Collections.emptyList());
    private static final DynamicURL DEFAULT_HTTPS_DYNAMIC_URL = new DynamicURL(Protocol.https, "localhost", null, Collections.emptyList());

    private static IllegalArgumentException invalidIPMessage() {
        return new IllegalArgumentException("IP address cannot be null or blank");
    }

    /**
     * Shorthand for parsedPath(Arrays.asList(path))
     */
    private static List<String> parsedPath(String... path) {
        return parsedPath(Arrays.asList(path));
    }

    /**
     * Takes every path String, validates it, splits it on '/', concatenates all the resulting lists
     */
    private static List<String> parsedPath(List<String> path) {
        return path.stream().flatMap(DynamicURL::parsedPathPart).collect(toList());
    }

    /**
     * Validates a path String and splits it on '/'
     * @throws IllegalArgumentException if path String contains '//' or is equal to '/' or empty
     * @throws AssertionError if the above conditions fail to hold in the resulting stream
     */
    private static Stream<String> parsedPathPart(String pathPart) {
        if (pathPart.contains("//"))
            throw new IllegalArgumentException("Paths cannot contain '//'");

        if (pathPart.startsWith("/"))
            pathPart = pathPart.substring(1);

        if (pathPart.endsWith("/"))
            pathPart = pathPart.substring(0, pathPart.length() - 1);

        if (pathPart.isEmpty())
            throw new IllegalArgumentException("Empty path parts not allowed");

        return Stream.of(pathPart.split("/"))
                .peek(DynamicURL::confirmAssertion);
    }

    /**
     * @throws AssertionError if given path part is empty, which should be avoided when constructing DynamicURL
     */
    private static void confirmAssertion(String pathPart) {
        if (pathPart.isEmpty())
            throw new AssertionError("Since there are no '//', no split strings can be empty");
    }

    /**
     * @throws IllegalArgumentException if given path part is a variable with empty name,
     * which should be avoided when constructing DynamicURL
     */
    private static void failOnEmptyVariable(String variable) {
        if (variable.equals(":"))
            throw new IllegalArgumentException("Variable must have a name and cannot be empty");
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return getUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DynamicURL)) return false;
        DynamicURL that = (DynamicURL) o;
        return Objects.equals(protocol, that.protocol) &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, ip, port, path);
    }

    // BUILDER

    /**
     * First step builder step, defines the protocol
     */
    public interface ProtocolStep {
        /**
         * Sets the protocol of the DynamicURL being built
         * @throws NullPointerException if protocol is null
         */
        IPStep protocol(Protocol protocol);
        /**
         * Sets the protocol of the DynamicURL being built to HTTP
         */
        IPStep HTTP();
        /**
         * Sets the protocol of the DynamicURL being built to HTTPS
         */
        IPStep HTTPS();
    }

    /**
     * Second step builder step, defines the IP address
     */
    public interface IPStep {
        /**
         * Sets the IP address of the DynamicURL being built
         * @return DynamicURL using the protocol from the first step and IP address from this step
         * @throws IllegalArgumentException if ip is null or blank
         */
        DynamicURL IP(String ip);
    }

    private static final class Builder implements ProtocolStep, IPStep {
        @Override
        public IPStep protocol(Protocol protocol) {
            Null.check(protocol).ifAny("Protocol cannot be null");
            if (protocol == Protocol.http)
                return HTTP();
            if (protocol == Protocol.https)
                return HTTPS();
            throw new AssertionError("There are only two protocols supported");
        }

        @Override
        public IPStep HTTP() {
            this.dynamicURL = DEFAULT_HTTP_DYNAMIC_URL;
            return this;
        }

        @Override
        public IPStep HTTPS() {
            this.dynamicURL = DEFAULT_HTTPS_DYNAMIC_URL;
            return this;
        }

        @Override
        public DynamicURL IP(String ip) {
            return dynamicURL.withIP(ip);
        }

        // PRIVATE

        private DynamicURL dynamicURL;
    }

}
