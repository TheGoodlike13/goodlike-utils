package eu.goodlike.functional;

import eu.goodlike.test.TestableRunnable;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CaseMatcherTest {

    private static final class AnotherRunnable implements Runnable {
        @Override
        public void run() {
            this.hasBeenRun = true;
        }

        public volatile boolean hasBeenRun = false;
    }

    @Test
    public void singleCaseGetsMatched() {
        TestableRunnable testableRunnable = new TestableRunnable();

        new CaseMatcher<Runnable>(TestableRunnable.class)
                .onCase(TestableRunnable.class, TestableRunnable::run)
                .match(testableRunnable);

        assertThat(testableRunnable.hasBeenRun())
                .isTrue();
    }

    @Test
    public void correctCaseGetsMatched() {
        TestableRunnable testableRunnable = new TestableRunnable();

        new CaseMatcher<>(AnotherRunnable.class, TestableRunnable.class)
                .onCase(AnotherRunnable.class, AnotherRunnable::run)
                .onCase(TestableRunnable.class, TestableRunnable::run)
                .match(testableRunnable);

        assertThat(testableRunnable.totalTimesRun())
                .isEqualTo(1);
    }

    @Test
    public void bothCasesGetsMatched() {
        TestableRunnable testableRunnable = new TestableRunnable();
        AnotherRunnable anotherRunnable = new AnotherRunnable();

        new CaseMatcher<>(AnotherRunnable.class, TestableRunnable.class)
                .onCase(AnotherRunnable.class, AnotherRunnable::run)
                .onCase(TestableRunnable.class, TestableRunnable::run)
                .match(testableRunnable, anotherRunnable);

        assertThat(testableRunnable.totalTimesRun())
                .isEqualTo(1);
        assertThat(anotherRunnable.hasBeenRun)
                .isTrue();
    }

    @Test
    public void exceptionThrownOnInterfaceOrAbstractClassMatcher() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> new CaseMatcher<>(Runnable.class));
    }

    @Test
    public void exceptionThrownOnMissingDefinitions() {
        CaseMatcher.Builder<Runnable> matcher = new CaseMatcher<>(TestableRunnable.class, AnotherRunnable.class)
                .onCase(TestableRunnable.class, TestableRunnable::run);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> matcher.match(new TestableRunnable()));
    }

    @Test
    public void exceptionThrownOnMultipleDefinitions() {
        CaseMatcher.Builder<Runnable> matcher = new CaseMatcher<Runnable>(TestableRunnable.class)
                .onCase(TestableRunnable.class, TestableRunnable::run);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> matcher.onCase(TestableRunnable.class, TestableRunnable::run));
    }

    @Test
    public void exceptionThrownOnNotMatchableDefinition() {
        CaseMatcher<Runnable> matcher = new CaseMatcher<>(TestableRunnable.class);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> matcher.onCase(AnotherRunnable.class, AnotherRunnable::run));
    }

    @Test
    public void exceptionThrownOnNotMatchableInstance() {
        CaseMatcher.Builder<Runnable> matcher = new CaseMatcher<Runnable>(TestableRunnable.class)
                .onCase(TestableRunnable.class, TestableRunnable::run);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> matcher.match(new AnotherRunnable()));
    }

}