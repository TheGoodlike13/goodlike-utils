package eu.goodlike.libraries.okhttp.cookies;

import eu.goodlike.functional.FutureCompleter;
import eu.goodlike.listener.ListenerRegistry;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieExpiryListenerTest {

    private static final FutureCompleter FUTURE_COMPLETER = new FutureCompleter(Duration.ofSeconds(1),
            Executors.newSingleThreadExecutor());

    private ListenerRegistry<CookieExpiryListener> listenerRegistry;
    private CookieStore cookieStore;

    @Before
    public void setup() {
        listenerRegistry = new ListenerRegistry<>();
        cookieStore = new StandardCookieStore(listenerRegistry, Duration.ofDays(1));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        FUTURE_COMPLETER.close();
    }

    @Test
    public void expiredCookiesTriggerListeners() throws Exception {
        CompletableFuture<Cookie> expiredCookieFutureContainer = new CompletableFuture<>();
        CookieExpiryListener listener = (cookie, lastUpdate) -> expiredCookieFutureContainer.complete(cookie);
        listenerRegistry.addListener(listener);

        HttpUrl url = HttpUrl.parse("http://google.com");
        Cookie expiredCookie = Cookie.parse(url, "name=value; Expires=Fri, 01 Jul 2016 00:00:00 GMT");

        cookieStore.putCookie(expiredCookie);
        FUTURE_COMPLETER.ensureCompletion(expiredCookieFutureContainer, "Listener for expired cookies was never called");

        assertThat(expiredCookieFutureContainer.join())
                .isEqualTo(expiredCookie);
    }

}