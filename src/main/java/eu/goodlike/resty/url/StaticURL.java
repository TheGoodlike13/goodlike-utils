package eu.goodlike.resty.url;

import eu.goodlike.neat.Null;
import eu.goodlike.resty.http.HttpMethod;
import eu.goodlike.resty.http.HttpRequest;
import eu.goodlike.resty.http.steps.BodyTypeStep;
import eu.goodlike.resty.http.steps.QueryParamStep;
import eu.goodlike.resty.http.steps.custom.CustomStaticStepBuilder;

import java.util.Objects;

import static eu.goodlike.resty.http.HttpMethod.*;

/**
 * <pre>
 * This class represents a static URL
 *
 * This URL is expected to be verified and usable (i.e. from a REST API response)
 *
 * Additional params can be added to it as a part of HTTP request creation process
 *
 * Objects of this class are immutable
 * </pre>
 */
public final class StaticURL {

    /**
     * @return url String of this StaticURL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return step builder using this StaticURL and HTTP GET method
     */
    public QueryParamStep GET() {
        return withGET();
    }

    /**
     * @return step builder using this StaticURL and HTTP POST method
     */
    public BodyTypeStep POST() {
        return withPOST();
    }

    /**
     * @return step builder using this StaticURL and HTTP PUT method
     */
    public BodyTypeStep PUT() {
        return withPUT();
    }

    /**
     * @return step builder using this StaticURL and HTTP DELETE method
     */
    public BodyTypeStep DELETE() {
        return withDELETE();
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * step builder methods instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this StaticURL and given HttpMethod
     * @throws NullPointerException if httpMethod is null
     */
    public HttpRequest sending(HttpMethod httpMethod) {
        Null.check(httpMethod).ifAny("HTTP method cannot be null");
        return new HttpRequest(httpMethod, url);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * GET() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this StaticURL and HTTP GET method
     */
    public HttpRequest withGET() {
        return sending(GET);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * POST() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this StaticURL and HTTP POST method
     */
    public HttpRequest withPOST() {
        return sending(POST);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * PUT() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this StaticURL and HTTP PUT method
     */
    public HttpRequest withPUT() {
        return sending(PUT);
    }

    /**
     * <pre>
     * This method allows a more direct access to the HttpRequest, though it is significantly preferable to use
     * DELETE() instead
     *
     * In general, only use this if you know what you're doing
     * </pre>
     * @return HttpRequest using this StaticURL and HTTP DELETE method
     */
    public HttpRequest withDELETE() {
        return sending(DELETE);
    }

    /**
     * <pre>
     * Only certain format urls are supported:
     *      1) ALL query or fragment parts are truncated (that is, anything after the first '?' and/or '#')
     *      2) The url must start with "http://" or "https://"
     *      3) Everything between this start and the next '/' or end of String is considered its IP
     *      4) Everything beyond the first '/' is considered its path (normal path parsing rules apply)
     * </pre>
     * @return DynamicURL version of this StaticURL, using getUrl() method as basis
     * @throws IllegalArgumentException if url format is not supported
     */
    public DynamicURL toDynamic() {
        return DynamicURL.of(this);
    }

    /**
     * <pre>
     * Custom step builder pattern enabling method
     *
     * By implementing CustomStaticStepBuilder (or extending AbstractStaticStepBuilder), you can create a custom
     * builder which allows to fluently construct a custom HttpRequest (or any of its steps) or even get an HttpResponse
     *
     * The builder should implement FirstStep (which should be an interface) if you are going to use AbstractStaticStepBuilder
     * </pre>
     */
    public <FirstStep> FirstStep custom(CustomStaticStepBuilder<FirstStep> customStaticStepBuilder) {
        return customStaticStepBuilder.setup(this);
    }

    // CONSTRUCTORS

    /**
     * Shorthand for new StaticURL(url)
     * @throws NullPointerException if url is null
     */
    public static StaticURL of(String url) {
        return new StaticURL(url);
    }

    /**
     * @return StaticURL version of given DynamicURL; uses getUrl() method as basis
     * @throws NullPointerException if dynamicURL is null
     */
    public static StaticURL of(DynamicURL dynamicURL) {
        Null.check(dynamicURL).ifAny("DynamicURL cannot be null");
        return of(dynamicURL.getUrl());
    }

    /**
     * Public constructor; use this if you prefer
     * @throws NullPointerException if url is null
     */
    public StaticURL(String url) {
        Null.check(url).ifAny("URL cannot be null");
        this.url = url;
    }

    // PRIVATE

    private final String url;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return getUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StaticURL)) return false;
        StaticURL staticURL = (StaticURL) o;
        return Objects.equals(url, staticURL.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

}
