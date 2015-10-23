package eu.goodlike.retry;

import eu.goodlike.functional.some.Some;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleRetryTest {

    private SimpleRetry<Integer> retry;
    private AtomicInteger integer;
    private List<Integer> failedIntegers;

    @Before
    public void setup() {
        integer = new AtomicInteger(0);
        failedIntegers = new ArrayList<>();
        retry = new SimpleRetry<>(integer::incrementAndGet, Long.MAX_VALUE, 1, 1, false, i -> i < 5,
                either -> either.ifFirstKind(failedIntegers::add)
                        .ifSecondThrowIt(RuntimeException::new)
                        .ifNeitherThrow(NullPointerException::new));
    }

    @Test
    public void tryRetrying_shouldRetryWhileFailingAndExecuteAppropriateAction() {
        Optional<Integer> retryResult = retry.getRetryResult();
        assertThat(retryResult).isPresent().contains(integer.intValue());
        assertThat(failedIntegers).isEqualTo(Some.ints().oneUpTo(4));
    }

}
