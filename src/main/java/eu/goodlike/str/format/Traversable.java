package eu.goodlike.str.format;

import java.util.Optional;

/**
 * <pre>
 * Represents an object that contains string values when following certain steps
 *
 * This object bears similarity to JSON format, just without arrays
 * </pre>
 */
public interface Traversable {

    /**
     * @return value after given steps, Optional::empty if any of the steps does not exist
     * @throws NullPointerException if firstStep is null
     * @throws NullPointerException if otherSteps is or contains null
     */
    Optional<String> getValueAt(String firstStep, String... otherSteps);

}
