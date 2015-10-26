package eu.goodlike.resty;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import eu.goodlike.neat.Null;
import eu.goodlike.resty.http.steps.BodyTypeStep;
import eu.goodlike.resty.http.steps.QueryParamStep;
import eu.goodlike.resty.url.DynamicURL;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * Loading cache wrapper for endpoint creation
 *
 * Rather than having to manually store the endpoints, you can use this cache; first, set up a seed, which will
 * be an universal prefix for all endpoints, i.e.
 *      DynamicURL apiPrefix = DynamicURL.builder().HTTPS().IP(api.twitch.tv).withPath("kraken");
 *      Endpoints twitchApi = Endpoints.of(apiPrefix);
 * then call:
 *      DynamicURL channelEndpoint = twitchApi.at("/channels/:channel");
 * or directly:
 *      twitchApi.GET("/channels/some_channel");
 *
 * Cache uses softValues(), so memory should be fine
 * </pre>
 */
public final class Endpoints {

    /**
     * @return the seed of these Endpoints
     */
    public DynamicURL at() {
        return seed;
    }

    /**
     * @return seed.appendPath(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public DynamicURL at(String... path) {
        Null.checkAlone(path).ifAny("Path array cannot be null");
        return at(Arrays.asList(path));
    }

    /**
     * @return seed.appendPath(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public DynamicURL at(List<String> path) {
        return urlCache.get(path);
    }

    /**
     * @return seed.GET()
     */
    public QueryParamStep GET() {
        return seed.GET();
    }

    /**
     * @return seed.GET(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public QueryParamStep GET(String... path) {
        return at(path).GET();
    }

    /**
     * @return seed.GET(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public QueryParamStep GET(List<String> path) {
        return at(path).GET();
    }

    /**
     * @return seed.POST()
     */
    public BodyTypeStep POST() {
        return seed.POST();
    }

    /**
     * @return seed.POST(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public BodyTypeStep POST(String... path) {
        return at(path).POST();
    }

    /**
     * @return seed.POST(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public BodyTypeStep POST(List<String> path) {
        return at(path).POST();
    }

    /**
     * @return seed.PUT()
     */
    public BodyTypeStep PUT() {
        return seed.PUT();
    }

    /**
     * @return seed.PUT(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public BodyTypeStep PUT(String... path) {
        return at(path).PUT();
    }

    /**
     * @return seed.PUT(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public BodyTypeStep PUT(List<String> path) {
        return at(path).PUT();
    }

    /**
     * @return seed.DELETE()
     */
    public BodyTypeStep DELETE() {
        return seed.DELETE();
    }

    /**
     * @return seed.DELETE(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public BodyTypeStep DELETE(String... path) {
        return at(path).DELETE();
    }

    /**
     * @return seed.DELETE(path)
     * @throws NullPointerException if path array is or contains null
     * @throws IllegalArgumentException if any of paths contains '//' or is equal to '/' or empty
     */
    public BodyTypeStep DELETE(List<String> path) {
        return at(path).DELETE();
    }

    // CONSTRUCTORS

    /**
     * Constructs a cache of DynamicURLs, which uses the seed as basis and appends paths to it when needed; uses
     * DynamicURL.of(String) to construct the initial DynamicURL
     * @throws NullPointerException if seedUrl is null
     * @throws IllegalArgumentException if url format is not supported
     */
    public static Endpoints of(String seedUrl) {
        return new Endpoints(DynamicURL.of(seedUrl));
    }

    /**
     * Constructs a cache of DynamicURLs, which uses the seed as basis and appends paths to it when needed
     * @throws NullPointerException if seed is null
     */
    public static Endpoints of(DynamicURL seed) {
        return new Endpoints(seed);
    }

    public Endpoints(DynamicURL seed) {
        Null.check(seed).ifAny("Seed URL cannot be null");
        this.seed = seed;
        this.urlCache = Caffeine.newBuilder()
                .softValues()
                .build(seed::appendPath);
    }

    // PRIVATE

    private final DynamicURL seed;
    private final LoadingCache<List<String>, DynamicURL> urlCache;

}
