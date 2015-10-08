package eu.goodlike.resty.http.steps;

import eu.goodlike.resty.misc.Header;

import java.util.Collection;

/**
 * <pre>
 * Step of HTTP request builder, defines what headers should be sent with the request
 *
 * Content type header will already be set when body type is chosen, if this request has a body
 *
 * You can skip this step and move directly to ResultStep
 * </pre>
 */
public interface HeaderStep extends ResultStep {

    /**
     * Sets a header of this request, overwriting previous header, if any
     * @throws NullPointerException if name of value are null
     * @throws IllegalArgumentException if name is blank
     */
    HeaderStep header(String name, String value);

    /**
     * Sets headers of this request, overwriting previous headers, if any
     * @throws NullPointerException if headers is or contains null
     */
    HeaderStep headers(Header... headers);

    /**
     * Sets headers of this request, overwriting previous headers, if any
     * @throws NullPointerException if headers is or contains null
     */
    HeaderStep headers(Collection<Header> headers);

}
