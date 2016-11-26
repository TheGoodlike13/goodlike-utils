package eu.goodlike.libraries.okhttp.cookies;

import com.google.common.net.InternetDomainName;
import eu.goodlike.functional.ImmutableCollectors;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.List;

/**
 * <pre>
 * CookieJar implementation which stores and matches cookies using model similar to RFC 6265
 *
 * Main difference is that a cookie from public domain is not allowed even if the URL host matches the domain exactly
 * </pre>
 */
public final class DomainAwareCookieJar implements CookieJar {

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookies.forEach(DomainAwareCookieJar::assertNotPublicDomain);
        cookieStore.putCookies(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.getCookies()
                .filter(cookie -> cookie.matches(url))
                .collect(ImmutableCollectors.toList());
    }

    // CONSTRUCTORS

    public DomainAwareCookieJar(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    // PRIVATE

    private final CookieStore cookieStore;

    private static void assertNotPublicDomain(Cookie cookie) {
        if (InternetDomainName.from(cookie.domain()).isPublicSuffix())
            throw new IllegalStateException("Cookie with explicit public domain: " + cookie);
    }

}
