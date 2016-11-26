package eu.goodlike.libraries.okhttp.cookies;

import okhttp3.Cookie;

import java.time.Instant;

/**
 * Executes arbitrary code when a cookie expires
 */
public interface CookieExpiryListener {

    /**
     * <pre>
     * Executes code when the cookie expires
     *
     * Implementation of this method should check if the cookie has appropriate values (i.e. name, domain, etc) and
     * then execute whatever action is needed
     *
     * This method is executed asynchronously and thus allow further modification of the cookie store (i.e. refresh
     * a cookie)
     * </pre>
     * @param cookie the expired cookie
     * @param lastUpdate the time the cookie was last updated; if this cookie has not been updated, its creation time
     *                   is passed instead
     */
    void onExpiredCookie(Cookie cookie, Instant lastUpdate);

}
