package eu.goodlike.functional;

import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class EithersTest {

    @Test
    public void leftStreamPackingWorks() {
        Either<Stream<Integer>, Integer> either1 = Either.left(Stream.of(1, 2, 3));
        Either<Stream<Integer>, Integer> either2 = Either.left(Stream.iterate(4, i -> i + 1));

        Stream<Either<Stream<Integer>, Integer>> superStream = Stream.of(either1, either2);

        assertThat(Eithers.packLeft(superStream).getLeft().limit(5))
                .containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    public void rightStreamPackingWorks() {
        Either<Integer, Stream<Integer>> either1 = Either.right(Stream.of(1, 2, 3));
        Either<Integer, Stream<Integer>> either2 = Either.right(Stream.iterate(4, i -> i + 1));

        Stream<Either<Integer, Stream<Integer>>> superStream = Stream.of(either1, either2);

         assertThat(Eithers.packRight(superStream).getRight().limit(5))
                .containsExactly(1, 2, 3, 4, 5);
    }

}
