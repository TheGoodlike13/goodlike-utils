package eu.goodlike.neat.string;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StrTest {

    @Test
    public void tryNoArgs_shouldBeEqualToNoArgsConstructor() {
        assertThat(Str.of()).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void trySomeArgs_shouldBeEqualToStringBuilderWithSameArgsConstructor() {
        Object o1 = "test";
        Object o2 = 2;
        StringBuilder builder = new StringBuilder().append(o1).append(o2);
        assertThat(Str.of(o1, o2)).isEqualTo(new StringBuilderWrapper(builder));
    }

    @Test
    public void trySomeArgsWithStringBuilder_shouldBeEqualToSameStringBuilderWithSameArgsConstructor() {
        Object o1 = "test";
        Object o2 = 3;
        StringBuilder builder1 = new StringBuilder("this is ");
        StringBuilder builder2 = new StringBuilder(builder1).append(o1).append(o2);
        assertThat(Str.of(builder1, o1, o2)).isEqualTo(new StringBuilderWrapper(builder2));
    }

}
