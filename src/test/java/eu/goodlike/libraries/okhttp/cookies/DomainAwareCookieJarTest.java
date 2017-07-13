package eu.goodlike.libraries.okhttp.cookies;

import com.google.common.collect.ImmutableList;
import eu.goodlike.listener.ListenerRegistry;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainAwareCookieJarTest {

    private CookieJar cookieJar;

    @Before
    public void setup() {
        cookieJar = new DomainAwareCookieJar(new StandardCookieStore(new ListenerRegistry<>(), Duration.ofDays(1)));
    }

    private HttpUrl urlForDomain(String domain) {
        return new HttpUrl.Builder()
                .scheme("http")
                .host(domain)
                .build();
    }

    @Test
    public void noCookiesArePreLoaded() {
        HttpUrl url = urlForDomain("customsforge.com");

        assertThat(cookieJar.loadForRequest(url))
                .isEmpty();
    }

    @Test
    public void hostOnlyCookiesCanBeLoadedFromTopDomain() {
        HttpUrl topDomainUrl = urlForDomain("customsforge.com");
        Cookie cookie = Cookie.parse(topDomainUrl, "name=value");
        cookieJar.saveFromResponse(topDomainUrl, ImmutableList.of(cookie));

        assertThat(cookieJar.loadForRequest(topDomainUrl))
                .containsExactly(cookie);
    }

    @Test
    public void hostOnlyCookiesCannotBeLoadedFromSubDomain() {
        HttpUrl topDomainUrl = urlForDomain("customsforge.com");
        Cookie cookie = Cookie.parse(topDomainUrl, "name=value");
        cookieJar.saveFromResponse(topDomainUrl, ImmutableList.of(cookie));

        HttpUrl subDomainUrl = urlForDomain("ignition.customsforge.com");
        assertThat(cookieJar.loadForRequest(subDomainUrl))
                .isEmpty();
    }

    @Test
    public void domainAwareCookiesCanBeLoadedFromTopDomain() {
        HttpUrl topDomainUrl = urlForDomain("customsforge.com");
        Cookie cookie = Cookie.parse(topDomainUrl, "name=value; Domain=customsforge.com");
        cookieJar.saveFromResponse(topDomainUrl, ImmutableList.of(cookie));

        assertThat(cookieJar.loadForRequest(topDomainUrl))
                .containsExactly(cookie);
    }

    @Test
    public void domainAwareCookiesCanBeLoadedFromSubDomain() {
        HttpUrl topDomainUrl = urlForDomain("customsforge.com");
        Cookie cookie = Cookie.parse(topDomainUrl, "name=value; Domain=customsforge.com");
        cookieJar.saveFromResponse(topDomainUrl, ImmutableList.of(cookie));

        HttpUrl subDomainUrl = urlForDomain("ignition.customsforge.com");
        assertThat(cookieJar.loadForRequest(subDomainUrl))
                .containsExactly(cookie);
    }

}