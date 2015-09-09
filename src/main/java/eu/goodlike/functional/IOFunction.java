package eu.goodlike.functional;

import java.io.IOException;

/**
 * Function which throws IOException
 */
@FunctionalInterface
public interface IOFunction<T, R> {

    /**
     * Same as Function.apply(t), except it throws an IOException
     */
    R apply(T t) throws IOException;

}
