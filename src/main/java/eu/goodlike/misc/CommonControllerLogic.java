package eu.goodlike.misc;

import eu.goodlike.time.TimeResolver;

import java.util.function.Function;

import static eu.goodlike.misc.Constants.DEFAULT_PAGE;
import static eu.goodlike.misc.Constants.DEFAULT_PER_PAGE;

/**
 * Basic validation methods, which apply to most controllers I use; they can be overwritten for special cases
 */
public interface CommonControllerLogic {

    default <T extends Throwable> void validateId(String source, long id, Function<String, T> exceptionSupplier) throws T {
        if (id <= 0)
            throw exceptionSupplier.apply(source + " id must be positive, not: " + id);
    }

    /**
     * <pre>
     * Defaults are set up below rather than in annotation because otherwise
     *
     *      ...?page=&per_page=
     *
     * would cause a NumberFormatException instead of using default values; this is fixed by:
     * 1) setting the field to Integer rather than int, causing empty param to default to null rather than
     *    fail to parse an empty string;
     * 2) since null would now be a valid value, it would no longer be overridden by @RequestParam(defaultValue)
     *    and would instead propagate down;
     * </pre>
     */
    default <T extends Throwable> int validatePage(Integer page, Function<String, T> exceptionSupplier) throws T {
        if (page == null)
            return DEFAULT_PAGE;

        if (page <= 0)
            throw exceptionSupplier.apply("Pages can only be positive, not " + page);

        return page - 1; //Pages start with 1 in the URL, but start with 0 in the app logic
    }

    /**
     * <pre>
     * Defaults are set up below rather than in annotation because otherwise
     *
     *      ...?page=&per_page=
     *
     * would cause a NumberFormatException instead of using default values; this is fixed by:
     * 1) setting the field to Integer rather than int, causing empty param to default to null rather than
     *    fail to parse an empty string;
     * 2) since null would now be a valid value, it would no longer be overridden by @RequestParam(defaultValue)
     *    and would instead propagate down;
     * </pre>
     */
    default <T extends Throwable> int validatePerPage(Integer per_page, Function<String, T> exceptionSupplier) throws T {
        if (per_page == null)
            return DEFAULT_PER_PAGE;

        if (per_page <= 0 || per_page > 100)
            throw exceptionSupplier.apply("You can only request 1-100 items per page, not " + per_page);

        return per_page;
    }

    /**
     * Uses start and end if they are given, otherwise resolves the time from timezone, startDate and endDate
     */
    default <T extends Throwable> TimeResolver validateTime(String timezone, String startDate, String endDate,
                                                            Long start, Long end,
                                                            Function<String, T> exceptionSupplier) throws T {
        TimeResolver timeResolver = TimeResolver.from(timezone, startDate, endDate, start, end);
        start = timeResolver.getStartTime();
        end = timeResolver.getEndTime();

        if (start < 0)
            throw exceptionSupplier.apply("Negative epoch milliseconds not allowed for start: " + start);

        if (end < 0)
            throw exceptionSupplier.apply("Negative epoch milliseconds not allowed for end: " + end);

        if (start >= end)
            throw exceptionSupplier.apply("End time should be after start time: " + start + " >=" + end);

        return timeResolver;
    }

}
