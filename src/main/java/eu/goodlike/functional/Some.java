package eu.goodlike.functional;

import eu.goodlike.functional.impl.some.IntSampler;
import eu.goodlike.functional.impl.some.LongSampler;

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
 *      List list = Some.of(this::getSomethingForInt).zeroTo(limit);
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
    public static <T> LongSampler<T> Of(LongFunction<T> anyFunction) {
        return new LongSampler<>(anyFunction);
    }

    /**
     * @return sampler of just integers
     */
    public static IntSampler<Integer> ints() {
        return of(i -> i);
    }

    /**
     * @return sampler of just longs
     */
    public static IntSampler<Long> longs() {
        return of(Long::valueOf);
    }

    /**
     * @return sampler of just strings of integers
     */
    public static IntSampler<String> strings() {
        return of(String::valueOf);
    }

    /**
     * @return sampler of just an integer
     */
    public static IntSampler<Integer> ofInt(int integer) {
        return of(i -> integer);
    }

    /**
     * @return sampler of just a long
     */
    public static IntSampler<Long> ofLong(long longInteger) {
        return of(i -> longInteger);
    }

    // PRIVATE

    private Some() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
