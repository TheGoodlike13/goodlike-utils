package eu.goodlike.functional.some;

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
     * @return sampler of just integers
     */
    public static IntSampler<Integer> ints() {
        return of(i -> i);
    }

    /**
     * @return sampler for a long function
     */
    public static <T> LongSampler<T> ofLong(LongFunction<T> anyFunction) {
        return new LongSampler<>(anyFunction);
    }

    /**
     * @return sampler of just longs
     */
    public static LongSampler<Long> longs() {
        return ofLong(i -> i);
    }

    // PRIVATE

    private Some() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
