package eu.goodlike.neat;

import eu.goodlike.neat.impl.StringBuilderWrapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StrTest {

    private final String o1 = "test";
    private final int o2 = 1;

    @Test
    public void tryNoArgs_shouldBeEqualToNoArgsConstructor() {
        assertThat(Str.of()).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void trySomeArgs_shouldBeEqualToStringBuilderWithSameArgsConstructor() {
        StringBuilder builder = new StringBuilder().append(o1).append(o2);
        assertThat(Str.of(o1, o2)).isEqualTo(new StringBuilderWrapper(builder));
    }

    @Test
    public void trySomeArgsWithStringBuilder_shouldBeEqualToSameStringBuilderWithSameArgsConstructor() {
        StringBuilder builder1 = new StringBuilder("this is ");
        StringBuilder builder2 = new StringBuilder(builder1).append(o1).append(o2);
        assertThat(Str.of(builder1, o1, o2)).isEqualTo(new StringBuilderWrapper(builder2));
    }

}
