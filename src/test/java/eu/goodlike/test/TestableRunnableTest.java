package eu.goodlike.test;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestableRunnableTest {

    @Test
    public void runnableCanTellIfItWasRun() {
        TestableRunnable testableRunnable = new TestableRunnable();
        testableRunnable.run();

        assertThat(testableRunnable.hasBeenRun())
                .isTrue();
    }

    @Test
    public void runnableCanTellIfItWasNotRun() {
        TestableRunnable testableRunnable = new TestableRunnable();

        assertThat(testableRunnable.hasBeenRun())
                .isFalse();
    }

    @Test
    public void runnableCanTellAmountOfTimesItHasBeenRun() {
        TestableRunnable testableRunnable = new TestableRunnable();
        testableRunnable.run();
        testableRunnable.run();

        assertThat(testableRunnable.totalTimesRun())
                .isEqualTo(2);
    }

    @Test
    public void runnableCanPretendToHaveBeenRunCertainAmountOfTimes() {
        TestableRunnable testableRunnable = new TestableRunnable(10);

        assertThat(testableRunnable.totalTimesRun())
                .isEqualTo(10);
    }

    @Test
    public void runnableWithPretendTimesRunCorrectlyAddsAdditionalRuns() {
        TestableRunnable testableRunnable = new TestableRunnable(10);
        testableRunnable.run();

        assertThat(testableRunnable.totalTimesRun())
                .isEqualTo(11);
    }

}