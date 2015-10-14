package eu.goodlike.validation;

import eu.goodlike.functional.Action;
import eu.goodlike.validation.impl.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <pre>
 * Allows to validate certain variables in a fluent way
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
 *      Validate.string(string)
 *          .custom(predicate1)
 *          .test().custom(predicate2).or().custom(predicate3).testEnd()
 *          .custom(predicate4)
 *          .ifInvalid(...)
 * testEnd() can be omitted if it's the last call before ifInvalid()
 * </pre>
 * @param <T> type of the object that is being validated
 * @param <V> validator for type T
 * @deprecated please use eu.goodlike.v2.validate.Validate instead!
 */
@SuppressWarnings("deprecation")
public abstract class Validate<T, V extends Validate<T, V>> {

    /**
     * <pre>
     * Should be extended to:
     *     return this;
     *
     * It is needed so that the methods from this validator can return the correct type when chaining
     * </pre>
     */
    protected abstract V thisValidator();

    /**
     * <pre>
     * Should be extended to:
     *     return new V(object);
     *
     * It is needed so that test() method can return a sub-validator which will simulate brackets
     * </pre>
     */
    protected abstract V newValidator();

    /**
     * @return the value being validated; useful when doing a validation across all elements of a collection
     */
    public final T value() {
        return object;
    }

    /**
     * Adds a custom predicate for type T
     */
    public final V custom(Predicate<T> customPredicate) {
        registerCondition(customPredicate);
        return thisValidator();
    }

    /**
     * Adds a predicate which tests if the object being validated is null
     */
    public final V Null() {
        registerCondition(t -> t == null);
        return thisValidator();
    }

    /**
     * <pre>
     * Separator for predicates; predicates to left and right of this separator will be evaluated like this:
     *      custom(predicate1).and().custom(predicate2)
     *      predicate1 && predicate2
     *
     * This separator is the default separator, and therefore is optional, in other words
     *      custom(predicate1).and().custom(predicate2)
     *      custom(predicate1).custom(predicate2)
     * are equivalent
     * </pre>
     */
    public final V and() {
        return thisValidator();
    }

    /**
     * <pre>
     * Separator for predicates; predicates to left and right of this separator will be evaluated like this:
     *      custom(predicate1).or().custom(predicate2)
     *      predicate1 || predicate2
     *
     * Unlike and(), this separator is NOT default and therefore has to be explicitly used
     * </pre>
     * @throws IllegalStateException if or() is the first statement of the validator or sub-validator (after test())
     */
    public final V or() {
        if (subConditions.isEmpty())
            throw new IllegalStateException("There must be at least a single condition before every or()");

        updateMainCondition();
        resetSubConditions();
        return thisValidator();
    }

    /**
     * <pre>
     * Negates the following predicate
     *
     * Using not().not() will cancel itself out
     * </pre>
     */
    public final V not() {
        notCondition = !notCondition;
        return thisValidator();
    }

    /**
     * <pre>
     * Simulates brackets for a boolean expression, specifically the opening brackets:
     *      test().custom(predicate1).custom(predicate2).testEnd()
     *      (predicate1 && predicate2)
     *
     * The operations inside the brackets are performed irrespectively to the operations outside the brackets in
     * regards to boolean operator priority; however, the left to right evaluation rule still applies, for example:
     *      predicate1 && (predicate2 || predicate3)
     * predicate1 is evaluated first, and if it is false, the brackets no longer get evaluated
     *
     * You can consider the brackets a single, large predicate in of itself, which follows the same rules as all
     * other predicates
     * </pre>
     */
    public final V test() {
        V validator = newValidator();
        validator.outerValidator = thisValidator();
        return validator;
    }

    /**
     * <pre>
     * Simulates brackets for a boolean expression, specifically the closing brackets:
     *      test().custom(predicate1).custom(predicate2).testEnd()
     *      (predicate1 && predicate2)
     *
     * The operations inside the brackets are performed irrespectively to the operations outside the brackets in
     * regards to boolean operator priority; however, the left to right evaluation rule still applies, for example:
     *      predicate1 && (predicate2 || predicate3)
     * predicate1 is evaluated first, and if it is false, the brackets no longer get evaluated
     *
     * You can consider the brackets a single, large predicate in of itself, which follows the same rules as all
     * other predicates;
     *
     * You can skip this method if it's the last method before ifInvalid(); this applies even in the case of multiple
     * brackets, i.e.
     *      test().custom(predicate1).test().custom(predicate2).testEnd().testEnd().ifInvalid(...)
     *      test().custom(predicate1).test().custom(predicate2).ifInvalid(...)
     * are equivalent
     * </pre>
     * @throws IllegalStateException if testEnd() is used more times than test(), in other words, dangling closing
     * brackets
     * @throws IllegalStateException if there are no conditions inside the brackets, i.e.
     *      predicate1 && () && predicate2
     * is not allowed
     */
    public final V testEnd() {
        if (outerValidator == null)
            throw new IllegalStateException("You must use test() before using testEnd()");

        if (!subConditions.isEmpty())
            updateMainCondition();

        if (condition == null)
            throw new IllegalStateException("You must have at least one condition between test() and testEnd()");

        V validator = outerValidator;
        outerValidator.registerCondition(condition);
        outerValidator = null;
        return validator;
    }

    /**
     * <pre>
     * Evaluates all the predicates for the object at the start of the predicate chain
     *
     * If the result of predicates is true, the object is considered valid; if the result of predicates is false,
     * the object is considered invalid, and an exception that is supplied to this method is thrown
     * </pre>
     * @param <X> type of exception to be thrown if the object being validated is invalid
     * @throws X if the object being validated is invalid
     * @throws IllegalStateException if there are no predicates to validate, i.e. validate(string).ifInvalid(...)
     */
    public final <X extends Throwable> void ifInvalid(Supplier<? extends X> exceptionSupplier) throws X {
        if (outerValidator != null) {
            testEnd().ifInvalid(exceptionSupplier);
            return;
        }

        if (isInvalid())
            throw exceptionSupplier.get();
    }

    /**
     * <pre>
     * Evaluates all the predicates for the object at the start of the predicate chain
     *
     * If the result of predicates is true, the object is considered valid; if the result of predicates is false,
     * the object is considered invalid, and an action that is supplied to this method is executed
     * </pre>
     * @throws IllegalStateException if there are no predicates to validate, i.e. validate(string).ifInvalid(...)
     */
    public final void ifInvalid(Action invalidAction) {
        if (outerValidator != null) {
            testEnd().ifInvalid(invalidAction);
            return;
        }

        if (isInvalid())
            invalidAction.doIt();
    }

    /**
     * This method CAN throw exceptions or execute arbitrary actions if the predicates passed to the validator
     * allow such things; in general, prefer to simply test validity
     * @return predicate, which will return true for valid objects and false for invalid objects
     * @throws IllegalStateException if there are no predicates to validate, i.e. validate(string).toPredicate()
     */
    public final Predicate<T> toPredicate() {
        if (outerValidator != null)
            return testEnd().toPredicate();

        if (!subConditions.isEmpty())
            updateMainCondition();

        if (condition == null)
            throw new IllegalStateException("You must have at least one condition to validate something!");

        return condition;
    }

    /**
     * This method CAN throw exceptions or execute arbitrary actions if the predicates passed to the validator
     * allow such things; in general, prefer to simply test validity
     * @return true if object is invalid, false otherwise
     * @throws IllegalStateException if there are no predicates to validate, i.e. validate(string).isInvalid()
     */
    public final boolean isInvalid() {
        if (outerValidator != null)
            return testEnd().isInvalid();

        return !toPredicate().test(object);
    }

    // CONSTRUCTORS

    @Deprecated
    public static ObjectValidator Object(Object o) {
        return new ObjectValidator(o);
    }

    @Deprecated
    public static BooleanValidator Boolean(Boolean bool) {
        return new BooleanValidator(bool);
    }

    @Deprecated
    public static CharArrayValidator charArray(char[] charArray) {
        return new CharArrayValidator(charArray);
    }

    @Deprecated
    public static StringValidator string(String string) {
        return new StringValidator(string);
    }

    @Deprecated
    public static IntValidator Int(Integer integer) {
        return new IntValidator(integer);
    }

    @Deprecated
    public static LongValidator Long(Long longNumber) {
        return new LongValidator(longNumber);
    }

    @Deprecated
    public static BigDecimalValidator bigDecimal(BigDecimal bigDecimal) {
        return new BigDecimalValidator(bigDecimal);
    }

    @Deprecated
    public static <T> CollectionValidator<T> collection(Collection<T> collection) {
        return new CollectionValidator<>(collection);
    }

    protected Validate(T object) {
        this.object = object;
    }

    // PROTECTED

    protected final T object;

    protected V outerValidator;

    /**
     * Adds a predicate to the list of conditions; this list is reduced using && operator when or() is called
     */
    protected void registerCondition(Predicate<T> predicate) {
        subConditions.add(notCondition ? predicate.negate() : predicate);
        notCondition = false;
    }

    // PRIVATE

    private Predicate<T> condition;
    private List<Predicate<T>> subConditions = new ArrayList<>();
    private boolean notCondition = false;

    private void updateMainCondition() {
        if (condition == null)
            condition = accumulatedCondition();
        else
            condition = condition.or(accumulatedCondition());
    }

    private void resetSubConditions() {
        subConditions = new ArrayList<>();
    }

    private Predicate<T> accumulatedCondition() {
        return subConditions.stream().reduce(Predicate::and)
                .orElseThrow(() -> new IllegalStateException("Cannot accumulate an empty list"));
    }

}
