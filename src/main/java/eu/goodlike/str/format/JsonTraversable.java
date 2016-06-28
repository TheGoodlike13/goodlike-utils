package eu.goodlike.str.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import eu.goodlike.neat.Null;

import java.util.Optional;

/**
 * Traversable implementation for JsonNode
 */
public final class JsonTraversable implements Traversable {

    @Override
    public Optional<String> getValueAt(String firstStep, String... otherSteps) {
        Null.check(firstStep).ifAny("First step cannot be null");
        Null.checkArray(otherSteps).ifAny("Steps cannot be or contain null");

        Optional<JsonNode> subNode = getSubNode(jsonNode, firstStep);
        for (String step : otherSteps)
            subNode = subNode.flatMap(node -> getSubNode(node, step));

        return subNode.map(JsonNode::asText);
    }

    // CONSTRUCTORS

    public JsonTraversable(JsonNode jsonNode) {
        Null.check(jsonNode).ifAny("Json node cannot be null");
        this.jsonNode = jsonNode;
    }

    // PRIVATE

    private final JsonNode jsonNode;

    private Optional<JsonNode> getSubNode(JsonNode node, String key) {
        JsonNode subNode = node.get(key);
        return subNode == null || NullNode.getInstance().equals(subNode)
                ? Optional.empty()
                : Optional.of(subNode);
    }

}
