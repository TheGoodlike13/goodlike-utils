package eu.goodlike.test;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestableIntConsumerTest {

    @Test
    public void consumerCanTellIfItHasAcceptedAnything() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer();
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.hasAcceptedAny())
                .isTrue();
    }

    @Test
    public void consumerCanTellIfItHasNotAcceptedAnything() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer();

        assertThat(testableIntConsumer.hasAcceptedAny())
                .isFalse();
    }

    @Test
    public void consumerCanTellAmountOfItemsAccepted() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer();
        testableIntConsumer.accept(1);
        testableIntConsumer.accept(2);

        assertThat(testableIntConsumer.totalTimesAccepted())
                .isEqualTo(2);
    }

    @Test
    public void consumerCountsAcceptsOfSameValueMultipleTimes() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer();
        testableIntConsumer.accept(1);
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.totalTimesAccepted())
                .isEqualTo(2);
    }

    @Test
    public void consumerCanTellIfSpecificItemWasAccepted() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer();
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.hasAccepted(1))
                .isTrue();
    }

    @Test
    public void consumerCanTellIfSpecificItemWasNotAccepted() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer();
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.hasAccepted(2))
                .isFalse();
    }

    @Test
    public void consumerCountsTimeSpecificValueHasBeenAccepted() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer();
        testableIntConsumer.accept(1);
        testableIntConsumer.accept(1);
        testableIntConsumer.accept(2);

        assertThat(testableIntConsumer.timesAccepted(1))
                .isEqualTo(2);
    }

    @Test
    public void consumerCanPretendToHaveAcceptedValues() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer(1);

        assertThat(testableIntConsumer.hasAccepted(1))
                .isTrue();
    }

    @Test
    public void consumerWithPretendAcceptedValuesContinuesToCountCorrectly() {
        TestableIntConsumer testableIntConsumer = new TestableIntConsumer(1);
        testableIntConsumer.accept(1);

        assertThat(testableIntConsumer.timesAccepted(1))
                .isEqualTo(2);
    }

}