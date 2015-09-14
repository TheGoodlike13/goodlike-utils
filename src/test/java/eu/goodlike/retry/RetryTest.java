package eu.goodlike.retry;

import eu.goodlike.random.Random;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class RetryTest {

    @Test
    public void test() throws Exception {
        Random.init();
        System.out.println("RNG ready");

        Optional<Integer> integer = Retry.This(() -> Random.getFast().number(1000))
                .maxTimes(5)
                .timeout().increasing().from(1).upTo(4).seconds()
                .failWhen(number -> number > 600)
                .or().failWhen(number -> number < 300)
                .onError(ex -> System.out.println("Exception! " + ex.getMessage()))
                .and().onNull(() -> System.out.println("How could RNG return null???"))
                .and().onInvalid(i -> System.out.println(i + " sucks!"))
                .doAsync()
                .get();

        if (integer.isPresent())
            assertTrue("Should succeed with ints only between 300 and 600", integer.get() >= 300 && integer.get() <= 600);

        System.out.println("Result: " + integer);
    }

}
