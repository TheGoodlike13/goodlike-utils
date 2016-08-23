package eu.goodlike.functional;

import eu.goodlike.test.TestableSupplier;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionalsTest {

    @Test
    public void lazyStreamIsLazy() {
        TestableSupplier<Optional<String>> empty = new TestableSupplier<>(Optional.empty());
        TestableSupplier<Optional<String>> firstNotEmpty = new TestableSupplier<>(Optional.of("This should be queried"));
        TestableSupplier<Optional<String>> secondNotEmpty = new TestableSupplier<>(Optional.of("This should not be queried"));

        Optionals.lazyStream(empty, firstNotEmpty, secondNotEmpty)
                .findFirst();

        assertThat(empty.hasBeenQueried())
                .isTrue();

        assertThat(firstNotEmpty.hasBeenQueried())
                .isTrue();

        assertThat(secondNotEmpty.hasBeenQueried())
                .isFalse();
    }

    @Test
    public void lazyFirstNotEmptyIsLazy() {
        TestableSupplier<Optional<String>> empty = new TestableSupplier<>(Optional.empty());
        TestableSupplier<Optional<String>> firstNotEmpty = new TestableSupplier<>(Optional.of("This should be queried"));
        TestableSupplier<Optional<String>> secondNotEmpty = new TestableSupplier<>(Optional.of("This should not be queried"));

        Optional<String> first = Optionals.lazyFirstNotEmpty(empty, firstNotEmpty, secondNotEmpty);

        assertThat(empty.hasBeenQueried())
                .isTrue();

        assertThat(firstNotEmpty.hasBeenQueried())
                .isTrue();

        assertThat(secondNotEmpty.hasBeenQueried())
                .isFalse();

        assertThat(first)
                .isEqualTo(firstNotEmpty.get());
    }

}