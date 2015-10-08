package eu.goodlike.resty.http.steps;

import java.io.IOException;

/**
 * Step of HTTP request builder, only available when JSON() was chosen in BodyTypeStep
 */
public interface JsonBodyStep {

    /**
     * Sets the request body to the raw given String; it is expected this String to be JSON (since the content type
     * will be set to "application/json;charset=UTF-8", but no attempts will be made to verify
     * @throws NullPointerException if string is null
     */
    HeaderStep raw(String string);

    /**
     * Sets the request body to given Object, parsed using Jackson; any value permitted by Jackson is permitted here
     */
    HeaderStep parse(Object jsonObject) throws IOException;

}
