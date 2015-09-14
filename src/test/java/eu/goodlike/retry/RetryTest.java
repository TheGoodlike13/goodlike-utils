package eu.goodlike.retry;

import eu.goodlike.random.Random;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class RetryTest {

    @Test
    public void test() throws Exception {
        Optional<Integer> integer = Retry.This(() -> Random.getFast().number(1000))
                .maxTimes(5)
                .timeout().increasing().from(1).upTo(4).seconds()
                .failWhen(number -> number > 500)
                .or().failWhen(number -> number < 400)
                .onFail(either -> either.ifSecondKind(ex -> System.out.println("FAIL!")))
                .and().onFail(either -> either.ifFirstKind(i -> System.out.println(either.getFirstKind() + " sucks!")))
                .doAsync()
                .get();

        if (integer.isPresent())
            assertTrue("Should not succeed with ints less than 900", integer.get() >= 900);
    }

}
