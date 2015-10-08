package eu.goodlike.resty.http.steps;

import eu.goodlike.resty.misc.Param;

import java.util.Collection;

/**
 * <pre>
 * Step of HTTP request builder, defines what params should be sent with the request
 *
 * If this is a GET request, they will be appended to the URL, otherwise they will be sent using
 * "application/x-www-form-urlencoded" in the body
 *
 * You can skip this step and move directly to HeaderStep
 * </pre>
 */
public interface QueryParamStep extends HeaderStep {

    /**
     * Appends a param to this request
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    QueryParamStep param(String name, Object value);

    /**
     * Appends params to this request
     * @throws NullPointerException if params is or contains null
     */
    QueryParamStep params(Param... params);

    /**
     * Appends params to this request
     * @throws NullPointerException if params is or contains null
     */
    QueryParamStep params(Collection<Param> params);

}
