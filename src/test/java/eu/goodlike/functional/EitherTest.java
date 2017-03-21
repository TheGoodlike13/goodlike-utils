package eu.goodlike.functional;

import eu.goodlike.test.TestableConsumer;
import eu.goodlike.test.TestableRunnable;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class EitherTest {

    private final String leftValue = "test";
    private final int rightValue = 1;
    private final Either<String, Integer> left = Either.left(leftValue);
    private final Either<String, Integer> right = Either.right(rightValue);
    private final Either<String, Integer> neither = Either.neither();
    private final Predicate<Object> truePredicate = any -> true;
    private final Predicate<Object> falsePredicate = any -> false;

    private final TestableConsumer<String> leftConsumer = new TestableConsumer<>();
    private final TestableConsumer<Integer> rightConsumer = new TestableConsumer<>();
    private final TestableRunnable runnable = new TestableRunnable();

    @Test
    public void rightNullMeansLeft() {
        assertThat(Either.of(leftValue, null)).isEqualTo(left);
    }

    @Test
    public void leftNullMeansRight() {
        assertThat(Either.of(null, rightValue)).isEqualTo(right);
    }

    @Test
    public void bothNullMeansNeither() {
        assertThat(Either.of(null, null)).isEqualTo(neither);
    }

    @Test
    public void optionalValuesAreExtracted() {
        assertThat(Either.left(Optional.of(leftValue))).isEqualTo(Either.left(leftValue));
        assertThat(Either.right(Optional.of(rightValue))).isEqualTo(Either.right(rightValue));
        assertThat(Either.of(Optional.of(leftValue), Optional.empty())).isEqualTo(Either.of(leftValue, null));
        assertThat(Either.of(Optional.empty(), Optional.of(rightValue))).isEqualTo(Either.of(null, rightValue));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void tryBothKinds_shouldThrowIllegalArgument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Either.of(leftValue, rightValue));
    }

    @Test
    public void leftIsLeft() {
        assertThat(left.getLeft()).isEqualTo(leftValue);
        assertThat(left.toOptionalLeft()).contains(leftValue);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(left::getRight);
        assertThat(left.toOptionalRight()).isEmpty();

        assertThat(left.isLeft()).isTrue();
        assertThat(left.isRight()).isFalse();
        assertThat(left.isEither()).isTrue();
        assertThat(left.isNeither()).isFalse();

        assertThat(left.leftOrElse("not first")).isEqualTo(leftValue);
        assertThat(left.leftOrGet(() -> "not first")).isEqualTo(leftValue);
        assertThat(left.rightOrElse(2)).isEqualTo(2);
        assertThat(left.rightOrGet(() -> 2)).isEqualTo(2);

        assertThat(left.leftOrThrow(RuntimeException::new)).isEqualTo(leftValue);
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> left.rightOrThrow(RuntimeException::new));
    }

    @Test
    public void rightIsRight() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(right::getLeft);
        assertThat(right.toOptionalLeft()).isEmpty();

        assertThat(right.getRight()).isEqualTo(rightValue);
        assertThat(right.toOptionalRight()).contains(rightValue);

        assertThat(right.isLeft()).isFalse();
        assertThat(right.isRight()).isTrue();
        assertThat(right.isEither()).isTrue();
        assertThat(right.isNeither()).isFalse();

        assertThat(right.leftOrElse("not first")).isEqualTo("not first");
        assertThat(right.leftOrGet(() -> "not first")).isEqualTo("not first");
        assertThat(right.rightOrElse(2)).isEqualTo(rightValue);
        assertThat(right.rightOrGet(() -> 2)).isEqualTo(rightValue);

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> right.leftOrThrow(RuntimeException::new));
        assertThat(right.rightOrThrow(RuntimeException::new)).isEqualTo(rightValue);
    }

    @Test
    public void neitherIsNeither() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(neither::getLeft);
        assertThat(neither.toOptionalLeft()).isEmpty();

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(neither::getRight);
        assertThat(neither.toOptionalRight()).isEmpty();

        assertThat(neither.isLeft()).isFalse();
        assertThat(neither.isRight()).isFalse();
        assertThat(neither.isEither()).isFalse();
        assertThat(neither.isNeither()).isTrue();

        assertThat(neither.leftOrElse("not first")).isEqualTo("not first");
        assertThat(neither.leftOrGet(() -> "not first")).isEqualTo("not first");
        assertThat(neither.rightOrElse(2)).isEqualTo(2);
        assertThat(neither.rightOrGet(() -> 2)).isEqualTo(2);

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> neither.leftOrThrow(RuntimeException::new));
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> neither.rightOrThrow(RuntimeException::new));
    }

    @Test
    public void leftActions() {
        left.ifLeft(leftConsumer);
        assertThat(leftConsumer.totalTimesAccepted()).isEqualTo(1);

        left.ifRight(rightConsumer);
        assertThat(rightConsumer.totalTimesAccepted()).isEqualTo(0);

        left.ifNeither(runnable);
        assertThat(runnable.totalTimesRun()).isEqualTo(0);
    }

    @Test
    public void rightActions() {
        right.ifLeft(leftConsumer);
        assertThat(leftConsumer.totalTimesAccepted()).isEqualTo(0);

        right.ifRight(rightConsumer);
        assertThat(rightConsumer.totalTimesAccepted()).isEqualTo(1);

        right.ifNeither(runnable);
        assertThat(runnable.totalTimesRun()).isEqualTo(0);
    }

    @Test
    public void neitherActions() {
        neither.ifLeft(leftConsumer);
        assertThat(leftConsumer.totalTimesAccepted()).isEqualTo(0);

        neither.ifRight(rightConsumer);
        assertThat(rightConsumer.totalTimesAccepted()).isEqualTo(0);

        neither.ifNeither(runnable);
        assertThat(runnable.totalTimesRun()).isEqualTo(1);
    }

    @Test
    public void leftFilters() {
        assertThat(left.filterLeft(truePredicate)).isEqualTo(left);
        assertThat(left.filterRight(truePredicate)).isEqualTo(left);

        assertThat(left.filterLeft(falsePredicate)).isEqualTo(neither);
        assertThat(left.filterRight(falsePredicate)).isEqualTo(left);
    }

    @Test
    public void rightFilters() {
        assertThat(right.filterLeft(truePredicate)).isEqualTo(right);
        assertThat(right.filterRight(truePredicate)).isEqualTo(right);

        assertThat(right.filterLeft(falsePredicate)).isEqualTo(right);
        assertThat(right.filterRight(falsePredicate)).isEqualTo(neither);
    }

    @Test
    public void neitherFilters() {
        assertThat(neither.filterLeft(truePredicate)).isEqualTo(neither);
        assertThat(neither.filterRight(truePredicate)).isEqualTo(neither);

        assertThat(neither.filterLeft(falsePredicate)).isEqualTo(neither);
        assertThat(neither.filterRight(falsePredicate)).isEqualTo(neither);
    }

    @Test
    public void leftMap() {
        assertThat(left.mapLeft(String::length)).isEqualTo(Either.left(leftValue.length()));
        assertThat(left.mapRight(i -> i * 2)).isEqualTo(left);

        assertThat(left.flatMapLeft(s -> Either.left(s.length()))).isEqualTo(Either.left(leftValue.length()));
        assertThat(left.flatMapRight(i -> Either.right(i * 2))).isEqualTo(left);

        assertThat(left.flatMap((s, i) -> Either.left(s.length()))).isEqualTo(Either.left(leftValue.length()));
    }

    @Test
    public void rightMap() {
        assertThat(right.mapLeft(String::length)).isEqualTo(right);
        assertThat(right.mapRight(i -> i * 2)).isEqualTo(Either.right(rightValue * 2));

        assertThat(right.flatMapLeft(s -> Either.left(s.length()))).isEqualTo(right);
        assertThat(right.flatMapRight(i -> Either.right(i * 2))).isEqualTo(Either.right(rightValue * 2));

        assertThat(right.flatMap((s, i) -> Either.right(i * 2))).isEqualTo(Either.right(rightValue * 2));
    }

    @Test
    public void neitherMap() {
        assertThat(neither.mapLeft(String::length)).isEqualTo(neither);
        assertThat(neither.mapRight(i -> i * 2)).isEqualTo(neither);

        assertThat(neither.flatMapLeft(s -> Either.left(s.length()))).isEqualTo(neither);
        assertThat(neither.flatMapRight(i -> Either.right(i * 2))).isEqualTo(neither);

        assertThat(neither.flatMap((s, i) -> Either.left(1))).isEqualTo(Either.left(1));
    }

    @Test
    public void collapse() {
        BiFunction<String, Integer, Integer> collapseFunction = (s, i) -> {
            if (s == null && i == null)
                return 1;

            return s == null ? i * 2 : s.length();
        };

        assertThat(left.collapse(String::length, i -> i * 2)).contains(leftValue.length());
        assertThat(left.collapse(collapseFunction)).contains(leftValue.length());

        assertThat(right.collapse(String::length, i -> i * 2)).contains(rightValue * 2);
        assertThat(right.collapse(collapseFunction)).contains(rightValue * 2);

        assertThat(neither.collapse(String::length, i -> i * 2)).isEmpty();
        assertThat(neither.collapse(collapseFunction)).contains(1);
    }

    @Test
    public void leftConditionalThrows() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> left.ifLeftThrow(RuntimeException::new));
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> left.ifLeftThrowPass(RuntimeException::new));

        left.ifRightThrow(RuntimeException::new);
        left.ifRightThrowPass(i -> new RuntimeException(String.valueOf(i)));

        left.ifNeitherThrow(RuntimeException::new);
    }

    @Test
    public void rightConditionalThrows() {
        right.ifLeftThrow(RuntimeException::new);
        right.ifLeftThrowPass(RuntimeException::new);

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() ->  right.ifRightThrow(RuntimeException::new));
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> right.ifRightThrowPass(i -> new RuntimeException(String.valueOf(i))));

        right.ifNeitherThrow(RuntimeException::new);
    }

    @Test
    public void NeitherConditionalThrows() {
        neither.ifLeftThrow(RuntimeException::new);
        neither.ifLeftThrowPass(RuntimeException::new);

        neither.ifRightThrow(RuntimeException::new);
        neither.ifRightThrowPass(i -> new RuntimeException(String.valueOf(i)));

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> neither.ifNeitherThrow(RuntimeException::new));
    }

}
