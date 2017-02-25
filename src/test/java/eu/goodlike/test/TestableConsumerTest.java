package eu.goodlike.test;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestableConsumerTest {

    @Test
    public void consumerCanTellIfItHasAcceptedAnything() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>();
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.hasAcceptedAny())
                .isTrue();
    }

    @Test
    public void consumerCanTellIfItHasNotAcceptedAnything() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>();

        assertThat(testableIntConsumer.hasAcceptedAny())
                .isFalse();
    }

    @Test
    public void consumerCanTellAmountOfItemsAccepted() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>();
        testableIntConsumer.accept(1);
        testableIntConsumer.accept(2);

        assertThat(testableIntConsumer.totalTimesAccepted())
                .isEqualTo(2);
    }

    @Test
    public void consumerCountsAcceptsOfSameValueMultipleTimes() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>();
        testableIntConsumer.accept(1);
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.totalTimesAccepted())
                .isEqualTo(2);
    }

    @Test
    public void consumerCanTellIfSpecificItemWasAccepted() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>();
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.hasAccepted(1))
                .isTrue();
    }

    @Test
    public void consumerCanTellIfSpecificItemWasNotAccepted() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>();
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.hasAccepted(2))
                .isFalse();
    }

    @Test
    public void consumerCountsTimeSpecificValueHasBeenAccepted() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>();
        testableIntConsumer.accept(1);
        testableIntConsumer.accept(1);
        testableIntConsumer.accept(2);

        assertThat(testableIntConsumer.timesAccepted(1))
                .isEqualTo(2);
    }

    @Test
    public void consumerCanPretendToHaveAcceptedValues() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>(1);

        assertThat(testableIntConsumer.hasAccepted(1))
                .isTrue();
    }

    @Test
    public void consumerWithPretendAcceptedValuesContinuesToCountCorrectly() {
        TestableConsumer<Integer> testableIntConsumer = new TestableConsumer<>(1);
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.timesAccepted(1))
                .isEqualTo(2);
    }

}