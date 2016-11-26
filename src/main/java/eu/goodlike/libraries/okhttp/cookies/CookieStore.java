package eu.goodlike.libraries.okhttp.cookies;

import com.google.common.collect.ImmutableList;
import eu.goodlike.neat.Null;
import okhttp3.Cookie;

import java.util.List;
import java.util.stream.Stream;

/**
 * Stores cookies in accordance to the implementation specification
 */
public interface CookieStore {

    /**
     * Adds all the given cookies to the cookie store, replacing existing ones in accordance to the implementation
     * specification
     * @throws NullPointerException if cookieList is or contains null
     */
    void putCookies(List<Cookie> cookieList);

    /**
     * @return all cookies in accordance to the implementation specification; may not return cookies that were added
     * previously, i.e. because they have expired
     */
    Stream<Cookie> getCookies();

    /**
     * Add the given cookie to the cookie store, replacing existing one in accordance to the implementation specification
     * @throws NullPointerException if cookie is null
     */
    default void putCookie(Cookie cookie) {
        Null.check(cookie).ifAny("Cannot be null: cookie");
        putCookies(ImmutableList.of(cookie));
    }

}
