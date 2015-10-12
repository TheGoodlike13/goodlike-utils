package eu.goodlike.neat.string;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class StringBuilderWrapperTest {

    private final String o1 = "test";
    private final int o2 = 1;
    private final List<Integer> collection = Arrays.asList(1, 2, 3);

    private StringBuilderWrapper stringBuilderWrapper;

    @Before
    public void setup() {
        stringBuilderWrapper = new StringBuilderWrapper();
    }

    @Test
    public void tryAppendingNoArgs_shouldRemainSame() {
        assertThat(stringBuilderWrapper.and()).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingAnyArgs_shouldBeEqualToStringBuilderWithSameArgsConstructor() {
        StringBuilder stringBuilder = new StringBuilder().append(o1).append(o2);
        assertThat(stringBuilderWrapper.and(o1, o2)).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingEmptyCollection_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSome(emptyList())).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingEmptyCollectionWithSuffix_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSome(emptyList(), " ")).isEqualTo(new StringBuilderWrapper());
     }

    @Test
    public void tryAppendingEmptyCollectionWithPrefix_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSome(" ", emptyList())).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingEmptyCollectionWithPrefixAndSuffix_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSome(" ", emptyList(), " ")).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingEmptyCollectionWithCustomAppender_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSome(emptyList(), (builder, obj) -> builder.append(obj).append(obj)))
                .isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingCollection_shouldBeEqualToStringBuilderWithCollectionConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(stringBuilder::append);
        assertThat(stringBuilderWrapper.andSome(collection)).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithPrefix_shouldBeEqualToStringBuilderWithCollectionAndPrefixConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(obj -> stringBuilder.append(" ").append(obj));
        assertThat(stringBuilderWrapper.andSome(" ", collection)).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithSuffix_shouldBeEqualToStringBuilderWithCollectionAndSuffixConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(obj -> stringBuilder.append(obj).append(" "));
        assertThat(stringBuilderWrapper.andSome(collection, " ")).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithPrefixAndSuffix_shouldBeEqualToStringBuilderWithCollectionPrefixAndSuffixConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(obj -> stringBuilder.append(" ").append(obj).append(" "));
        assertThat(stringBuilderWrapper.andSome(" ", collection, " ")).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithCustomAppender_shouldBeEqualToStringBuilderWithCollectionCustomAppendedConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(obj -> stringBuilder.append(obj).append(obj));
        assertThat(stringBuilderWrapper.andSome(collection, (builder, obj) -> builder.append(obj).append(obj)))
                .isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingAnyArgsWithTrueCondition_shouldBeEqualToStringBuilderWithSameArgsConstructor() {
        StringBuilder stringBuilder = new StringBuilder().append(o1).append(o2);
        assertThat(stringBuilderWrapper.andIf(true, o1, o2)).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingAnyArgsWithFalseCondition_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andIf(false, o1, o2)).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingCollectionWithTrueCondition_shouldBeEqualToStringBuilderWithCollectionConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(stringBuilder::append);
        assertThat(stringBuilderWrapper.andSomeIf(true, collection)).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithFalseCondition_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSomeIf(false, collection)).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingCollectionWithPrefixAndTrueCondition_shouldBeEqualToStringBuilderWithCollectionAndPrefixConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(obj -> stringBuilder.append(" ").append(obj));
        assertThat(stringBuilderWrapper.andSomeIf(true, " ", collection)).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithPrefixAndFalseCondition_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSomeIf(false, " ", collection)).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingCollectionWithSuffixAndTrueCondition_shouldBeEqualToStringBuilderWithCollectionAndSuffixConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(obj -> stringBuilder.append(obj).append(" "));
        assertThat(stringBuilderWrapper.andSomeIf(true, collection, " ")).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithSuffixAndFalseCondition_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSomeIf(false, collection, " ")).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingCollectionWithPrefixSuffixAndTrueCondition_shouldBeEqualToStringBuilderWithCollectionPrefixAndSuffixConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(obj -> stringBuilder.append(" ").append(obj).append(" "));
        assertThat(stringBuilderWrapper.andSomeIf(true, " ", collection, " ")).isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithPrefixSuffixAndFalseCondition_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSomeIf(false, " ", collection, " ")).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void tryAppendingCollectionWithCustomAppenderAndTrueCondition_shouldBeEqualToStringBuilderWithCollectionCustomAppendedConstructor() {
        StringBuilder stringBuilder = new StringBuilder();
        collection.forEach(obj -> stringBuilder.append(obj).append(obj));
        assertThat(stringBuilderWrapper.andSomeIf(true, collection, (builder, obj) -> builder.append(obj).append(obj)))
                .isEqualTo(new StringBuilderWrapper(stringBuilder));
    }

    @Test
    public void tryAppendingCollectionWithCustomAppenderAndFalseCondition_shouldRemainSame() {
        assertThat(stringBuilderWrapper.andSomeIf(false, collection, (builder, obj) -> builder.append(obj).append(obj)))
                .isEqualTo(new StringBuilderWrapper());
    }

}
