package eu.goodlike.resty.http.steps;

import eu.goodlike.resty.misc.Param;

import java.io.File;
import java.util.Collection;

/**
 * <pre>
 * Step of HTTP request builder, only available when multipart() was chosen in BodyTypeStep
 *
 * This does not immediately construct a MultipartRequest, instead it is constructed at the final step (perform())
 *
 * You can skip this step and move directly to HeaderStep
 * </pre>
 */
public interface MultipartBodyStep extends HeaderStep {

    /**
     * Sets a field value to send in multipart body; it is saved in a Set of Param; if value is null, the param is skipped
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    MultipartBodyStep field(String name, Object value);

    /**
     * Sets field values to send in multipart body; they are saved in a Set of Param;
     * if value of a param is null, that param is skipped
     * @throws NullPointerException if params is or contains null
     */
    MultipartBodyStep fields(Param... params);

    /**
     * Sets field values to send in multipart body; they are saved in a Set of Param;
     * if value of a param is null, that param is skipped
     * @throws NullPointerException if params is or contains null
     */
    MultipartBodyStep fields(Collection<Param> params);

    /**
     * Sets a file to send in multipart body; it is saved in a Map of String to File
     * @throws NullPointerException if fieldName or uploadedFile is null
     */
    MultipartBodyStep file(String fieldName, File uploadedFile);

}
