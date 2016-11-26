package eu.goodlike.libraries.okhttp.cookies;

import eu.goodlike.listener.ListenerRegistry;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class StandardCookieStoreTest {

    private CookieStore cookieStore;

    @Before
    public void setup() {
        cookieStore = new StandardCookieStore(new ListenerRegistry<>(), Duration.ofDays(7));
    }

    @Test
    public void uponConstructionCookieStoreIsEmpty() {
        assertThat(cookieStore.getCookies())
                .isEmpty();
    }

    @Test
    public void addedCookieWillBeReturned() {
        HttpUrl url = HttpUrl.parse("http://google.com");
        Cookie cookie = Cookie.parse(url, "name=value");
        cookieStore.putCookie(cookie);

        assertThat(cookieStore.getCookies())
                .containsExactly(cookie);
    }

    @Test
    public void addingCookieWithExistingNamePathAndDomainReplacesOldCookie() {
        HttpUrl url = HttpUrl.parse("http://google.com");
        Cookie cookie = Cookie.parse(url, "name=value");
        cookieStore.putCookie(cookie);

        Cookie updatedCookie = Cookie.parse(url, "name=updated-value");
        cookieStore.putCookie(updatedCookie);

        assertThat(cookieStore.getCookies())
                .containsExactly(updatedCookie);
    }

    @Test
    public void multipleCookiesWithSameNameButDifferentDomainAreAllowed() {
        HttpUrl url = HttpUrl.parse("http://google.com");
        Cookie cookie = Cookie.parse(url, "name=value");
        cookieStore.putCookie(cookie);

        HttpUrl otherDomainUrl = HttpUrl.parse("http://not.quite.google.com");
        Cookie otherDomainCookie = Cookie.parse(otherDomainUrl, "name=updated-value; Domain=not.quite.google.com");
        cookieStore.putCookie(otherDomainCookie);

        assertThat(cookieStore.getCookies())
                .containsExactlyInAnyOrder(cookie, otherDomainCookie);
    }

    @Test
    public void multipleCookiesForSameDomainButNotBothHostOnlyAreAllowed() {
        HttpUrl url = HttpUrl.parse("http://google.com");
        Cookie hostOnlyCookie = Cookie.parse(url, "name=value");
        cookieStore.putCookie(hostOnlyCookie);

        Cookie domainSpecifiedCookie = Cookie.parse(url, "name=updated-value; Domain=google.com");
        cookieStore.putCookie(domainSpecifiedCookie);

        assertThat(cookieStore.getCookies())
                .containsExactlyInAnyOrder(hostOnlyCookie, domainSpecifiedCookie);
    }

    @Test
    public void multipleCookiesWithSameNameButDifferentPathAreAllowed() {
        HttpUrl url = HttpUrl.parse("http://google.com");
        Cookie defaultPathCookie = Cookie.parse(url, "name=value");
        cookieStore.putCookie(defaultPathCookie);

        Cookie specificPathCookie = Cookie.parse(url, "name=updated-value; Path=/search");
        cookieStore.putCookie(specificPathCookie);

        assertThat(cookieStore.getCookies())
                .containsExactlyInAnyOrder(defaultPathCookie, specificPathCookie);
    }

    @Test
    public void expiredCookiesAreNotStored() {
        HttpUrl url = HttpUrl.parse("http://google.com");
        Cookie expiredCookie = Cookie.parse(url, "name=value; Expires=Fri, 01 Jul 1970 00:00:00 GMT");
        cookieStore.putCookie(expiredCookie);

        assertThat(cookieStore.getCookies())
                .doesNotContain(expiredCookie);
    }

    @Test
    public void updatingWithExpiredCookieDeletesOriginalCookie() {
        HttpUrl url = HttpUrl.parse("http://google.com");
        Cookie validCookie = Cookie.parse(url, "name=value");
        cookieStore.putCookie(validCookie);

        Cookie expiredCookie = Cookie.parse(url, "name=value; Expires=Fri, 01 Jul 1970 00:00:00 GMT");
        cookieStore.putCookie(expiredCookie);

        assertThat(cookieStore.getCookies())
                .doesNotContain(validCookie, expiredCookie);
    }

}