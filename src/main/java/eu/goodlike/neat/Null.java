package eu.goodlike.neat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * <pre>
 * Simplifies checking if some parameters are null
 *
 * You usually have 2 alternatives:
 * 1) Allow nulls to propagate;
 *  This may not even be an option in some cases, but it is also obfuscating - NullPointerExceptions thrown only return
 *  the line where the exception was thrown, but this can be extremely confusing in case of chained statements or
 *  lambda expressions;
 * 2) Check for nulls at the start of the method, all in one statement
 *  This is very clunky (verbose), it also doesn't identify which exact parameter was null (unless you make it even
 *  more clunky or even separate the null checks for every parameter);
 *
 * With this you can check for nulls like this: *
 * Null.check(param1, param2, param3).ifAny("Param1, param2 and param3 should not be null");
 *
 * This will automatically turn the objects into a list which will be printed out if any of the arguments were null,
 * including your message, allowing to easily figure out the location of null(s)
 * </pre>
 */
public class Null {

    /**
     * <pre>
     * Allows customizing the kind of exception that is thrown using lambda expression:
     *      () -> new X()
     * </pre>
     * @throws X if any of the checked objects are null
     */
    public <X extends Throwable> void ifAny(Supplier<? extends X> exceptionSupplier) throws X {
        if (objects.contains(null))
            throw exceptionSupplier.get();
    }

    /**
     * @throws NullPointerException if any of the checked objects are null
     */
    public void ifAny(String message) {
        int index = objects.indexOf(null);
        if (index >= 0)
            throw new NullPointerException(message + "; parameter at index "
                    + index +" was null, please check: " + objects);
    }

    // CONSTRUCTORS

    public static Null check(Object... objects) {
        return objects == null ? DEFINITELY_NULL : new Null(Arrays.asList(objects));
    }

    public static Null check(Object singleObject) {
        return singleObject == null ? DEFINITELY_NULL : DEFINITELY_NOT_NULL;
    }

    /**
     * Use this method when you need to check something like a primitive array for null by itself
     * to avoid ambiguous call warnings
     */
    public static Null checkAlone(Object possiblyArray) {
        return possiblyArray == null ? DEFINITELY_NULL : DEFINITELY_NOT_NULL;
    }

    /**
     * Use this method when you need to check all array elements for null (including array itself)
     * to avoid ambiguous call warnings
     */
    public static Null checkArray(Object[] definitelyArray) {
        return definitelyArray == null ? DEFINITELY_NULL : check(definitelyArray);
    }

    /**
     * Use this method when you need to check all collection elements for null (including collection itself)
     */
    public static Null checkCollection(Collection<?> collection) {
        return collection == null ? DEFINITELY_NULL : new Null(new ArrayList<>(collection));
    }

    private Null(List<Object> objects) {
        this.objects = objects;
    }

    // PRIVATE

    private final List<Object> objects;

    private static final Null DEFINITELY_NULL = new DefinitelyNull();
    private static final Null DEFINITELY_NOT_NULL = new DefinitelyNotNull();

    // SUBCLASSES

    /**
     * <pre>
     * This is used when:
     *      a) a single object was checked for null
     *      b) this object was indeed null
     *
     * It avoids Null object construction and varargs array creation entirely
     * </pre>
     */
    public static final class DefinitelyNull extends Null {
        /**
         * <pre>
         * Allows customizing the kind of exception that is thrown using lambda expression:
         *      () -> new X()
         * </pre>
         * @throws X because this object is definitely null
         */
        public <X extends Throwable> void ifAny(Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }

        /**
         * @throws NullPointerException because this object is definitely null
         */
        public void ifAny(String message) {
            throw new NullPointerException(message);
        }

        private DefinitelyNull() {
            super(null);
        }
    }

    /**
     * <pre>
     * This is used when:
     *      a) a single object was checked for null
     *      b) this object was actually not null
     *
     * It avoids Null object construction and varargs array creation entirely
     * </pre>
     */
    public static final class DefinitelyNotNull extends Null {
        /**
         * <pre>
         * Allows customizing the kind of exception that is thrown using lambda expression:
         *      () -> new X()
         * </pre>
         * @throws X never, because this object is definitely not null
         */
        public <X extends Throwable> void ifAny(Supplier<? extends X> exceptionSupplier) throws X {}

        /**
         * @throws NullPointerException never because this object is definitely null
         */
        public void ifAny(String message) {}

        private DefinitelyNotNull() {
            super(null);
        }
    }

}
