package eu.goodlike.functional.some;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

/**
 * <pre>
 * An alternative to sequential loops:
 *      List list = new ArrayList();
 *      for (int i = 0; i < limit; i++)
 *          lost.add(getSomethingForInt(i));
 *
 * and primitive streams:
 *      List list = IntStream.range(0, limit)
 *              .mapToObj(this::getSomethingForInt)
 *              .collect(toList());
 *
 * which turns into:
 *      List list = Some.of(this::getSomethingForInt).get(limit);
 * </pre>
 */
public final class Some {

    /**
     * @return sampler for an integer function
     */
    public static <T> IntSampler<T> of(IntFunction<T> anyFunction) {
        return new IntSampler<>(anyFunction);
    }

    /**
     * @return sampler for a long function
     */
    public static <T> LongSampler<T> ofLong(LongFunction<T> anyFunction) {
        return new LongSampler<>(anyFunction);
    }

    /**
     * @return sampler for any function
     */
    public static <T, U> ObjectSampler<T, U> valuesOf(Function<T, U> anyFunction) {
        return new ObjectSampler<>(anyFunction);
    }

    // PRIVATE

    private Some() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
