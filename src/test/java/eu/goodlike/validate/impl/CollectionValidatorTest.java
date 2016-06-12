package eu.goodlike.validate.impl;

import eu.goodlike.functional.Some;
import eu.goodlike.validate.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static eu.goodlike.validate.Validate.anInt;
import static org.assertj.core.api.Assertions.assertThat;

public class CollectionValidatorTest {

    private final List<Integer> testList = Some.ints().oneUpTo(5);

    private CollectionValidator<Integer> validator;
    private AtomicInteger actionCounter;
    private List<Integer> consumerTester;

    @Before
    public void setup() {
        validator = Validate.Collections.ints();
        actionCounter = new AtomicInteger(0);
        consumerTester = new ArrayList<>();
    }

    @Test
    public void tryEqualsWithEqualList_shouldBeTrue() {
        assertThat(validator.isEqual(testList).test(Some.ints().oneUpTo(5))).isTrue();
    }

    @Test
    public void tryEqualsWithDifferentList_shouldBeFalse() {
        assertThat(validator.isEqual(testList).test(Some.ints().oneUpTo(4))).isFalse();
    }

    @Test
    public void tryEmptyOnEmptyList_shouldBeTrue() {
        assertThat(validator.isEmpty().test(new ArrayList<>())).isTrue();
    }

    @Test
    public void tryEmptyOnNonEmptyList_shouldBeFalse() {
        assertThat(validator.isEmpty().test(testList)).isFalse();
    }

    @Test
    public void tryAllMatchOnEmptyList_shouldBeTrue() {
        assertThat(validator.allMatch(i -> i == 0).test(new ArrayList<>())).isTrue();
    }

    @Test
    public void tryAllMatchOnMatchingList_shouldBeTrue() {
        assertThat(validator.allMatch(i -> i == 0).test(Some.ofInt(0).oneUpTo(5))).isTrue();
    }

    @Test
    public void tryAllMatchOnNonMatchingList_shouldBeFalse() {
        assertThat(validator.allMatch(i -> i == 0).test(Some.ints().zeroTo(5))).isFalse();
    }

    @Test
    public void tryAnyMatchOnEmptyList_shouldBeFalse() {
        assertThat(validator.anyMatch(i -> i == 0).test(new ArrayList<>())).isFalse();
    }

    @Test
    public void tryAnyMatchOnListWithMatch_shouldBeTrue() {
        assertThat(validator.anyMatch(i -> i == 0).test(Some.ints().zeroTo(5))).isTrue();
    }

    @Test
    public void tryAnyMatchOnListWithoutMatch_shouldBeFalse() {
        assertThat(validator.anyMatch(i -> i == 0).test(testList)).isFalse();
    }

    @Test
    public void tryForEachCustomAction_shouldExecuteActionAppropriateNumberOfTimes() {
        validator.forFailing(anInt().isAtMost(3)).doRun(actionCounter::incrementAndGet).test(testList);
        assertThat(actionCounter.get()).isEqualTo(2);
    }

    @Test
    public void tryForEachCustomConsumer_shouldConsumerForEveryMatcher() {
        validator.forFailing(anInt().isAtMost(3)).doAccept(consumerTester::add).test(testList);
        assertThat(consumerTester).isEqualTo(Some.ints().with(4, 5));
    }

    @Test(expected = RuntimeException.class)
    public void tryForAnyInvalidThrowWithInvalid_shouldThrow() {
        validator.forFailing(anInt().isAtMost(3)).doThrow(() -> new RuntimeException("Found int above 3!")).test(testList);
    }

    @Test
    public void tryForAnyInvalidThrowWithValid_shouldPass() {
        validator.forFailing(anInt().isAtMost(5)).doThrow(() -> new RuntimeException("Found int above 5!")).test(testList);
    }

    @Test(expected = RuntimeException.class)
    public void tryForAnyInvalidThrowConsumingWithInvalid_shouldThrow() {
        validator.forFailing(anInt().isAtMost(3)).doThrowWith(i -> new RuntimeException("Found int above 3: " + i)).test(testList);
    }

    @Test
    public void tryForAnyInvalidThrowConsumingWithValid_shouldPass() {
        validator.forFailing(anInt().isAtMost(5)).doThrowWith(i -> new RuntimeException("Found int above 5: " + i)).test(testList);
    }

}
