package eu.goodlike.libraries.okhttp.cookies;

import eu.goodlike.listener.ListenerRegistry;
import eu.goodlike.neat.Null;
import okhttp3.Cookie;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.CacheEntry;
import org.cache2k.event.CacheEntryExpiredListener;
import org.cache2k.expiry.ExpiryPolicy;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * <pre>
 * Stores cookies until they expire or size limit is reached
 *
 * All cookies are set to expire after a certain duration
 *
 * Expired cookies are removed immediately, even if they just overwrote an existing cookie
 *
 * Every cookie is identified by:
 *      their name
 *      their domain
 *      whether this domain was host only or not
 *      their path
 * </pre>
 */
public final class StandardCookieStore implements CookieStore {

    @Override
    public void putCookie(Cookie cookie) {
        Null.check(cookie).as("cookie");

        CookieId id = new CookieId(cookie);
        cookieCache.put(id, cookie);
    }

    @Override
    public void putCookies(List<Cookie> cookieList) {
        Null.checkList(cookieList).as("cookieList");

        Map<CookieId, Cookie> cookieMap = new HashMap<>();
        for (Cookie cookie : cookieList) {
            CookieId id = new CookieId(cookie);
            cookieMap.put(id, cookie);
        }

        cookieCache.putAll(cookieMap);
    }

    @Override
    public Stream<Cookie> getCookies() {
        return cookieMapView.values().stream();
    }

    // CONSTRUCTORS

    public StandardCookieStore(ListenerRegistry<CookieExpiryListener> listenerRegistry, Duration maxCookieDuration) {
        Null.check(listenerRegistry).as("listenerRegistry");
        this.cookieCache = Cache2kBuilder.of(CookieId.class, Cookie.class)
                .expireAfterWrite(maxCookieDuration.toNanos(), TimeUnit.NANOSECONDS)
                .expiryPolicy(EXPIRY_POLICY)
                .sharpExpiry(true)
                .addAsyncListener(new CachedCookieExpiryListener(listenerRegistry))
                .build();

        this.cookieMapView = this.cookieCache.asMap();
    }

    // PRIVATE

    private final Cache<CookieId, Cookie> cookieCache;
    private final Map<CookieId, Cookie> cookieMapView;

    private static final ExpiryPolicy<CookieId, Cookie> EXPIRY_POLICY = StandardCookieStore::expirePolicy;

    private static long expirePolicy(CookieId cookieId, Cookie cookie, long loadTime, CacheEntry<CookieId, Cookie> oldEntry) {
        return cookie.expiresAt();
    }

    private static final class CookieId {
        // CONSTRUCTORS

        private CookieId(Cookie cookie) {
            this.hostOnly = cookie.hostOnly();
            this.name = cookie.name();
            this.path = cookie.path();
            this.domain = cookie.domain();
        }

        // PRIVATE

        private final boolean hostOnly;
        private final String name;
        private final String path;
        private final String domain;

        // OBJECT OVERRIDES

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CookieId)) return false;
            CookieId cookieId = (CookieId) o;
            return Objects.equals(hostOnly, cookieId.hostOnly) &&
                    Objects.equals(name, cookieId.name) &&
                    Objects.equals(path, cookieId.path) &&
                    Objects.equals(domain, cookieId.domain);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hostOnly, name, path, domain);
        }
    }

    private static final class CachedCookieExpiryListener implements CacheEntryExpiredListener<CookieId, Cookie> {
        @Override
        public void onEntryExpired(Cache<CookieId, Cookie> cache, CacheEntry<CookieId, Cookie> entry) {
            Cookie cookie = entry.getValue();
            Instant lastUpdate = Instant.ofEpochMilli(entry.getLastModification());
            listenerRegistry.getAllListeners().forEach(listener -> listener.onExpiredCookie(cookie, lastUpdate));
        }

        // CONSTRUCTORS

        private CachedCookieExpiryListener(ListenerRegistry<CookieExpiryListener> listenerRegistry) {
            this.listenerRegistry = listenerRegistry;
        }

        // PRIVATE

        private final ListenerRegistry<CookieExpiryListener> listenerRegistry;
    }

}
