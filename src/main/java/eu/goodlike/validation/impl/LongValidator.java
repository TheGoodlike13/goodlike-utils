package eu.goodlike.validation.impl;

/**
 * <pre>
 * Validate implementation for longs
 *
 * Reasons for using this over @Valid:
 * 1) The validations seem all too basic, requiring either 3rd party annotations or custom made annotations for
 *    special cases; this seems daft, because it's simpler to just write plain old Java code; not to mention it
 *    is easier to debug this way;
 * 2) Copying the class for use in other projects, i.e. Android, libraries, etc, becomes cumbersome, because the
 *    dependencies of the annotations must be preserved; in most cases they are simply deleted;
 * 3) Allows for easier customization of error messages returned by the server, since custom exceptions can be
 *    thrown without significant hassle;
 * 4) Allows to test for incorrect input values prematurely as well, rather than waiting until an instance of the
 *    object was created, if such a need arises;
 *
 * Validation works by chaining predicates one after another; for example:
 *      Validate.string(string).not().Null().not().empty().ifInvalid(...);
 *
 * The predicates are evaluated left to right, using normal boolean operator priority rules;
 * the default operator between two predicates is "and()";
 * you have to use "or()" explicitly; also, in order to simulate brackets, such like:
 *      predicate1 && (predicate2 || predicate3) && predicate4
 * you can use test() and testEnd():
 *      Validate.Long(long)
 *          .custom(predicate1)
 *          .test().custom(predicate2).or().custom(predicate3).testEnd()
 *          .custom(predicate4)
 *          .ifInvalid(...)
 * testEnd() can be omitted if it's the last call before ifInvalid()
 * </pre>
 * @deprecated please use eu.goodlike.v2.validate.Validate instead!
 */
@Deprecated
@SuppressWarnings("deprecation")
public final class LongValidator extends eu.goodlike.validation.Validate<Long, LongValidator> {

    /**
     * Adds a predicate which tests if the long being validated is larger than some amount
     */
    public LongValidator moreThan(long amount) {
        registerCondition(i -> i > amount);
        return this;
    }

    /**
     * Adds a predicate which tests if the long being validated is smaller than some amount
     */
    public LongValidator lessThan(long amount) {
        registerCondition(i -> i < amount);
        return this;
    }

    /**
     * Adds a predicate which tests if the long being validated is larger or equal to some amount
     */
    public LongValidator atLeast(long amount) {
        registerCondition(i -> i >= amount);
        return this;
    }

    /**
     * Adds a predicate which tests if the long being validated is smaller or equal to some amount
     */
    public LongValidator atMost(long amount) {
        registerCondition(i -> i <= amount);
        return this;
    }

    /**
     * Adds a predicate which tests if the long being validated is equal to some amount
     */
    public LongValidator equal(long amount) {
        registerCondition(i -> i == amount);
        return this;
    }

    /**
     * Adds a predicate which tests if the long being validated is between some two numbers, both inclusive
     */
    public LongValidator between(long lowBound, long highBound) {
        registerCondition(i -> i >= lowBound && i <= highBound);
        return this;
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a day of the month
     *
     * This is equivalent to between(1, 31)
     *
     * While certain months do not have days 29 to 31, it is assumed that this is handled somewhere else
     * entirely; the purpose of this is simply to validate the input
     * </pre>
     */
    public LongValidator dayOfMonth() {
        registerCondition(i -> i >= 1 && i <= 31);
        return this;
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe an hour of day
     *
     * This is equivalent to between(0, 23)
     * </pre>
     */
    public LongValidator hourOfDay() {
        registerCondition(i -> i >= 0 && i <= 23);
        return this;
    }

    /**
     * <pre>
     * Adds a predicate which tests if the long can describe a minute of hour
     *
     * This is equivalent to between(0, 59)
     * </pre>
     */
    public LongValidator minuteOfHour() {
        registerCondition(i -> i >= 0 && i <= 59);
        return this;
    }

    // CONSTRUCTORS

    public LongValidator(Long object) {
        super(object);
    }

    // PROTECTED

    @Override
    protected LongValidator thisValidator() {
        return this;
    }

    @Override
    protected LongValidator newValidator() {
        return new LongValidator(object);
    }
}
