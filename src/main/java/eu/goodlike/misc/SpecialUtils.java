package eu.goodlike.misc;

import eu.goodlike.neat.Null;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Function;

/**
 * <pre>
 * Contains various utility methods, without association to any particular subject
 *
 * It was created to avoid multiple Utils classes just holding a single method, which doesn't easily translate
 * into a helper class
 * </pre>
 */
public final class SpecialUtils {

    /**
     * @return -1 if i1 < i2; 0 if i1 == i2; 1 if i1 > i2; null is considered to be the LARGEST number in this case
     * which will cause any collection, which is sorted using this comparator to put null to the very end
     */
    public static int compareNullableIntegers(Integer i1, Integer i2) {
        return i1 == null
                        ? i2 == null ? 0 : 1
                        : i2 == null ? -1 : Integer.compare(i1, i2);
    }

    /**
     * @return URL encoded string representation of an object; String.valueOf() is used to convert
     * @throws IllegalStateException if DEFAULT_CHARSET is not supported
     */
    public static String urlEncode(Object value) {
        try {
            return URLEncoder.encode(String.valueOf(value), Constants.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Please add support for " + Constants.DEFAULT_CHARSET, e);
        }
    }

    /**
     * <pre>
     * This method is useful when both of the objects are nullable, but you cannot compare them directly:
     *      equals(stringBuilder1, stringBuilder2, StringBuilder::toString)
     * and similar; don't forget to adjust the hashCode as well:
     *      stringBuilder == null ? null : stringBuilder.toString().hashCode()
     * </pre>
     * @return true if objects are equal given a certain transformation, false otherwise
     */
    public static <T, U> boolean equals(T t1, T t2, Function<T, U> converter) {
        Null.check(converter).ifAny("Converter cannot be null");
        return t1 == null ? t2 == null : converter.apply(t1).equals(converter.apply(t2));
    }

    /**
     * @return amount of cores that the JVM can see; if this value is less than minimumCores, minimumCores is returned
     * instead
     * @throws IllegalArgumentException if minimumCores < 1
     */
    public static int getCoreCountWithMin(int minimumCores) {
        if (minimumCores < 1)
            throw new IllegalArgumentException("Minimum cores must be positive, not " + minimumCores);

        int availableCores = Runtime.getRuntime().availableProcessors();
        return availableCores > minimumCores ? availableCores : minimumCores;
    }

    /**
     * <pre>
     * Runs given runnable when the program exits normally; may not run if JVM is aborted (refer to
     * Runtime::addShutdownHook for specifics)
     *
     * This method should only be used in special circumstances, i.e. when spawning child processes which should close
     * along with the application, or handling resources which are difficult to close, such as log files. In most other
     * scenarios, prefer try-with-resources or similar!
     * </pre>
     * @throws NullPointerException if runnable is null
     */
    public static void runOnExit(Runnable runnable) {
        Null.check(runnable).ifAny("Runnable cannot be null");
        Runtime.getRuntime().addShutdownHook(new Thread(runnable));
    }

    // PRIVATE

    private SpecialUtils() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
