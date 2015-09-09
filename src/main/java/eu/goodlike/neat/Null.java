package eu.goodlike.neat;

import java.util.Arrays;
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
public final class Null {

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
        if (index >= 0) {
            if (objects.size() > 1)
                message = message + "; parameter at index " + index + " was null, please check: " + objects;

            throw new NullPointerException(message);
        }
    }

    // CONSTRUCTORS

    public static Null check(Object... objects) {
        return new Null(objects);
    }

    private Null(Object... objects) {
        this.objects = Arrays.asList(objects);
    }

    // PRIVATE

    private final List<Object> objects;

}
