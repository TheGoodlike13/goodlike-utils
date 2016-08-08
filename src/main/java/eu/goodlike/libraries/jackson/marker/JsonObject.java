package eu.goodlike.libraries.jackson.marker;

import eu.goodlike.libraries.jackson.Json;

import java.io.IOException;

/**
 * Marker interface for objects, which can be turned into JSON by Jackson
 */
public interface JsonObject {

    /**
     * @return JSON string for this object
     * @throws IllegalStateException if this object cannot be parsed as JSON
     */
    default String toJson() {
        try {
            return Json.stringFrom(this);
        } catch (IOException e) {
            throw new IllegalStateException("Marked as JsonObject, but unable to parse as JSON: " + this.getClass(), e);
        }
    }

    /**
     * @return JSON bytes for this object
     * @throws IllegalStateException if this object cannot be parsed as JSON
     */
    default byte[] toJsonBytes() {
        try {
            return Json.bytesFrom(this);
        } catch (IOException e) {
            throw new IllegalStateException("Marked as JsonObject, but unable to parse as JSON: " + this.getClass(), e);
        }
    }

}
