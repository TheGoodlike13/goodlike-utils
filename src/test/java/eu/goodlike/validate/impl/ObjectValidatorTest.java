package eu.goodlike.validate.impl;

import eu.goodlike.functional.Some;
import eu.goodlike.validate.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectValidatorTest {

    private ObjectValidator<Integer> validator;
    private List<Integer> actionTester;

    @Before
    public void setup() {
        validator = Validate.a(Integer.class);
        actionTester = new ArrayList<>();
    }

    @Test
    public void tryPassesWithValid_shouldBeTrue() {
        assertThat(validator.passes(i -> i == 10).test(10)).isTrue();
    }

    @Test
    public void tryPassesWithInvalid_shouldBeFalse() {
        assertThat(validator.passes(i -> i == 10).test(11)).isFalse();
    }

    @Test
    public void tryContainedInArrayWithContained_shouldBeTrue() {
        assertThat(validator.isIn(1, 2, 3).test(2)).isTrue();
    }

    @Test
    public void tryContainedInArrayWithContained_shouldBeFalse() {
        assertThat(validator.isIn(1, 2, 3).test(4)).isFalse();
    }

    @Test
    public void tryContainedInCollectionWithContained_shouldBeTrue() {
        assertThat(validator.isIn(Some.ints().oneUpTo(3)).test(2)).isTrue();
    }

    @Test
    public void tryContainedInCollectionWithContained_shouldBeFalse() {
        assertThat(validator.isIn(Some.ints().oneUpTo(3)).test(4)).isFalse();
    }

    @Test
    public void tryIsInvalid_shouldBeSameAsNotTest() {
        assertThat(validator.isNull().test(4)).isEqualTo(!validator.isNull().isInvalid(4));
    }

    @Test
    public void tryInvalidCustomActionWithInvalid_shouldPerformAction() {
        validator.isNull().ifInvalid(6).thenRun(() -> actionTester.add(1));
        assertThat(actionTester).isEqualTo(Some.ofInt(1).oneUpTo(1));
    }

    @Test
    public void tryInvalidCustomActionWithValid_shouldDoNothing() {
        validator.isNull().ifInvalid(null).thenRun(() -> actionTester.add(1));
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryInvalidCustomConsumerWithInvalid_shouldConsume() {
        validator.isNull().ifInvalid(6).thenAccept(actionTester::add);
        assertThat(actionTester).isEqualTo(Some.ofInt(6).oneUpTo(1));
    }

    @Test
    public void tryInvalidCustomConsumerWithValid_shouldDoNothing() {
        validator.isNull().ifInvalid(null).thenAccept(actionTester::add);
        assertThat(actionTester).isEmpty();
    }

    @Test(expected = RuntimeException.class)
    public void tryInvalidThrowWithInvalid_shouldThrow() {
        validator.isNull().ifInvalid(6).thenThrow(() -> new RuntimeException("Not null found!"));
    }

    @Test
    public void tryInvalidThrowWithValid_shouldPass() {
        validator.isNull().ifInvalid(null).thenThrow(() -> new RuntimeException("Not null found!"));
    }

    @Test(expected = RuntimeException.class)
    public void tryInvalidThrowConsumingWithInvalid_shouldThrow() {
        validator.isNull().ifInvalid(6).thenThrowWith(i -> new RuntimeException("Not null found: " + i));
    }

    @Test
    public void tryInvalidThrowConsumingWithValid_shouldPass() {
        validator.isNull().ifInvalid(null).thenThrowWith(i -> new RuntimeException("Not null found: " + i));
    }

}
