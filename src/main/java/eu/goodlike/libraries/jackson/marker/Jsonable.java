package eu.goodlike.libraries.jackson.marker;

/**
 * Interface which defines how to create an object which then can be turned into JSON
 * @param <T> type of object which can turn into JSON
 */
public interface Jsonable<T extends JsonObject> {

    /**
     * @return object that represents values of this object and can be turned into JSON
     */
    T asJsonObject();

}
