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
                .as("check if testableRunnable has been run")
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
                .as("check how many times testableRunnable has been run")
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
                .as("check how many times testableRunnable has been run")
                .isEqualTo(1);
        assertThat(anotherRunnable.hasBeenRun)
                .as("check if anotherRunnable has been run")
                .isTrue();
    }

    @Test
    public void ignoredCaseDoesNotGetMatched() {
        TestableRunnable testableRunnable = new TestableRunnable();

        new CaseMatcher<Runnable>(TestableRunnable.class)
                .ignoreCase(TestableRunnable.class)
                .match(testableRunnable);

        assertThat(testableRunnable.hasBeenRun())
                .as("check if testableRunnable has been run")
                .isFalse();
    }

    @Test
    public void correctCaseGetsMapped() {
        TestableRunnable testableRunnable = new TestableRunnable();

        boolean result = new CaseMatcher<>(TestableRunnable.class, AnotherRunnable.class)
                .mapInto(Boolean.class)
                .onCase(TestableRunnable.class, true)
                .onCase(AnotherRunnable.class, false)
                .map(testableRunnable);

        assertThat(result)
                .as("check if correct case got matched")
                .isTrue();
    }

    @Test
    public void incorrectCaseGetsIgnored() {
        AnotherRunnable anotherRunnable = new AnotherRunnable();

        boolean result = new CaseMatcher<>(TestableRunnable.class, AnotherRunnable.class)
                .mapInto(Boolean.class)
                .onCase(TestableRunnable.class, true)
                .onCase(AnotherRunnable.class, false)
                .map(anotherRunnable);

        assertThat(result)
                .as("check if correct case got matched")
                .isFalse();
    }

    @Test
    public void exceptionThrownOnInterfaceOrAbstractClassMatcher() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> new CaseMatcher<>(Runnable.class));
    }

    @Test
    public void exceptionThrownOnMissingDefinitions() {
        CaseMatcher.MatcherBuilder<Runnable> matcher = new CaseMatcher<>(TestableRunnable.class, AnotherRunnable.class)
                .onCase(TestableRunnable.class, TestableRunnable::run);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> matcher.match(new TestableRunnable()));
    }

    @Test
    public void exceptionThrownOnMultipleDefinitions() {
        CaseMatcher.MatcherBuilder<Runnable> matcher = new CaseMatcher<Runnable>(TestableRunnable.class)
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
        CaseMatcher.MatcherBuilder<Runnable> matcher = new CaseMatcher<Runnable>(TestableRunnable.class)
                .onCase(TestableRunnable.class, TestableRunnable::run);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> matcher.match(new AnotherRunnable()));
    }

}