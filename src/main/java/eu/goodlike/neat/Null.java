package eu.goodlike.neat;

import eu.goodlike.neat.impl.nullcheck.*;

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
public abstract class Null {

    // ABSTRACT

    /**
     * @return index of first occurrence of null if tested object(s) contains null, -1 otherwise
     */
    protected abstract int indexOfNull();

    /**
     * @return String representation of tested object(s) to print out for debug purposes when any are null
     */
    protected abstract String contentToString();

    // PUBLIC

    /**
     * Convenience method to check if any of the items is null without throwing exception
     * @return true if any of elements being checked are null
     */
    public boolean containsNull() {
        return indexOfNull() >= 0;
    }

    /**
     * <pre>
     * Allows customizing the kind of exception that is thrown using lambda expression:
     *      () -> new X()
     * </pre>
     * @throws X if any of the checked objects are null
     * @throws NullPointerException is exceptionSupplier is null
     */
    public <X extends Throwable> void ifAny(Supplier<? extends X> exceptionSupplier) throws X {
        if (exceptionSupplier == null)
            throw new NullPointerException("Exception supplier cannot be null");

        if (containsNull())
            throw exceptionSupplier.get();
    }

    /**
     * @throws NullPointerException if any of the checked objects are null
     * @throws NullPointerException if message is null
     */
    public void ifAny(String message) {
        if (message == null)
            throw new NullPointerException("Message cannot be null");

        int index = indexOfNull();
        if (index >= 0)
            throw new NullPointerException(message + "; parameter at index "
                    + index + " was null, please check: " + contentToString());
    }

    // CONSTRUCTORS

    public static Null check(Object one) {
        return one == null ? DEFINITELY_NULL : DEFINITELY_NOT_NULL;
    }

    public static Null check(Object one, Object two) {
        return new TwoNull(one, two);
    }

    public static Null check(Object one, Object two, Object three) {
        return new ThreeNull(one, two, three);
    }

    public static Null check(Object one, Object two, Object three, Object four) {
        return new FourNull(one, two, three, four);
    }

    public static Null check(Object one, Object two, Object three, Object four, Object five) {
        return new FiveNull(one, two, three, four, five);
    }

    public static Null check(Object... objects) {
        return objects == null ? DEFINITELY_NULL : new ArrayNull(objects);
    }

    /**
     * Use this method when you need to check something like a primitive array for null by itself
     * to avoid ambiguous call warnings
     */
    public static Null checkAlone(Object possiblyArray) {
        return check(possiblyArray);
    }

    /**
     * Use this method when you need to check all array elements for null (including array itself)
     * to avoid ambiguous call warnings
     */
    public static Null checkArray(Object[] definitelyArray) {
        return definitelyArray == null ? DEFINITELY_NULL : new ArrayNull(definitelyArray);
    }

    /**
     * Use this method when you need to check all collection elements for null (including collection itself)
     */
    public static Null checkList(List<?> list) {
        return list == null ? DEFINITELY_NULL : new ListNull(list);
    }

    /**
     * Use this method when you need to check all collection elements for null (including collection itself)
     */
    public static Null checkCollection(Collection<?> collection) {
        return collection == null ? DEFINITELY_NULL : new CollectionNull(collection);
    }

    // PRIVATE

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
    private static final class DefinitelyNull extends Null {
        @Override
        protected int indexOfNull() {
            return 0;
        }

        @Override
        protected String contentToString() {
            return null;
        }

        /**
         * @return true because this object is definitely null
         */
        @Override
        public boolean containsNull() {
            return true;
        }

        /**
         * <pre>
         * Allows customizing the kind of exception that is thrown using lambda expression:
         *      () -> new X()
         * </pre>
         * @throws X because this object is definitely null
         * @throws NullPointerException is exceptionSupplier is null
         */
        @Override
        public <X extends Throwable> void ifAny(Supplier<? extends X> exceptionSupplier) throws X {
            if (exceptionSupplier == null)
                throw new NullPointerException("Exception supplier cannot be null");

            throw exceptionSupplier.get();
        }

        /**
         * @throws NullPointerException because this object is definitely null
         * @throws NullPointerException if message is null
         */
        @Override
        public void ifAny(String message) {
            if (message == null)
                throw new NullPointerException("Message cannot be null");

            throw new NullPointerException(message);
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
    private static final class DefinitelyNotNull extends Null {
        @Override
        protected int indexOfNull() {
            return -1;
        }

        @Override
        protected String contentToString() {
            return null;
        }

        /**
         * @return false because this object is definitely not null
         */
        @Override
        public boolean containsNull() {
            return false;
        }

        /**
         * <pre>
         * Allows customizing the kind of exception that is thrown using lambda expression:
         *      () -> new X()
         * </pre>
         * @throws X never, because this object is definitely not null
         * @throws NullPointerException is exceptionSupplier is null
         */
        @Override
        public <X extends Throwable> void ifAny(Supplier<? extends X> exceptionSupplier) throws X {
            if (exceptionSupplier == null)
                throw new NullPointerException("Exception supplier cannot be null");
        }

        /**
         * @throws NullPointerException never because this object is definitely not null
         * @throws NullPointerException if message is null
         */
        @Override
        public void ifAny(String message) {
            if (message == null)
                throw new NullPointerException("Message cannot be null");
        }
    }

}
