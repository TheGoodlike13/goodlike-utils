package eu.goodlike.str.format;

import eu.goodlike.misc.ArrayUtils;
import eu.goodlike.misc.impl.array.ArraySplitter;
import eu.goodlike.neat.Null;

import java.util.Optional;

/**
 * <pre>
 * Formats a string using a Traversable
 *
 * Given a string like this:
 *      {@literal "<replace:me> <with> <values:of:some:kind>"}
 *
 * It will first check the overrides Traversable, if such exists, and then the main Traversable using these steps:
 *      "replace", "me"                     -> "Just"
 *      "with"                              -> "an example"
 *      "values", "of", "some", "kind"      -> Optional::empty
 *
 * Then the values will replace the ones in the string:
 *      "Just an example "
 *
 * Notice how there is an extra empty space at the end - this is because values:of:some:kind didn't return anything, and
 * thus was replaced with an empty string
 * </pre>
 */
public final class TraversableFormatter {

    /**
     * @return String, formatted using the Traversables of this formatter, or Optional::empty if the string contains
     * any '<' without closing them with '>' (random ':' and '>' values are ignored, though)
     */
    public Optional<String> format(String string) {
        Null.check(string).ifAny("String cannot be null");

        StringBuilder builder = new StringBuilder();
        String subString = string;
        int stepStartIndex;
        while ((stepStartIndex = subString.indexOf(STEP_START)) >= 0) {
            builder.append(subString, 0, stepStartIndex);
            int stepEndIndex = subString.indexOf(STEP_END, stepStartIndex);
            if (stepEndIndex < 0)
                return Optional.empty();

            String key = subString.substring(stepEndIndex + 1, stepEndIndex);
            getValue(key).ifPresent(builder::append);

            subString = subString.substring(stepEndIndex + 1);
        }
        return Optional.of(builder.append(subString).toString());
    }

    // CONSTRUCTORS

    public TraversableFormatter(Traversable traversable) {
        this(traversable, null);
    }

    public TraversableFormatter(Traversable traversable, Traversable overrides) {
        Null.check(traversable).ifAny("Traversable cannot be null");
        this.traversable = traversable;
        this.overrides = overrides;
    }

    // PRIVATE

    private final Traversable traversable;
    private final Traversable overrides;

    private Optional<String> getValue(String key) {
        String[] paths = key.split(NEXT_STEP);

        ArraySplitter<String> splitter = ArrayUtils.split(paths);
        Optional<String> firstElement = splitter.getFirstElement();
        String[] elementsAfterFirst = splitter.getElementsAfterFirst();

        Optional<String> value = firstElement
                .flatMap(firstStep -> getValueFromOverrides(firstStep, elementsAfterFirst));
        return value.isPresent()
                ? value
                : firstElement
                .flatMap(firstStep -> traversable.getValueAt(firstStep, elementsAfterFirst));
    }

    private Optional<String> getValueFromOverrides(String firstStep, String... otherSteps) {
        return Optional.ofNullable(overrides)
                .flatMap(override -> override.getValueAt(firstStep, otherSteps));
    }

    private static final char STEP_START = '<';
    private static final char STEP_END = '>';
    private static final String NEXT_STEP = ":";

}
