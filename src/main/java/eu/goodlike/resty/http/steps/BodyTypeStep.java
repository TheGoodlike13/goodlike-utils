package eu.goodlike.resty.http.steps;

/**
 * <pre>
 * Step of HTTP request builder, defines what kind of object to use as POST, PUT or DELETE body
 *
 * Here are the possible scenarios:
 *      1) With noBody(), nothing will be sent as body, and you can move on directly to headers
 *      2) With JSON(), you will be asked to give either a raw String, or an Object which can be parsed with Jackson
 *      3) With multipart(), MultipartRequest will be used (at the final step) and you will be asked to specify
 *         params and files to send
 *      4) With form(), you will be asked to give params, which will be sent like a query String in the body
 * </pre>
 */
public interface BodyTypeStep {

    /**
     * Sends no body with this request, immediately move on to headers
     */
    HeaderStep noBody();

    /**
     * Sends a JSON body with this request, sets content type to "application/json;charset=UTF-8"
     * and asks for a raw JSON String (it is not verified) or an Object that can be parsed using Jackson
     */
    JsonBodyStep JSON();

    /**
     * Sends multipart request, sets content type to "multipart/form-data" and asks for form fields/files to send in body
     */
    MultipartBodyStep multipart();

    /**
     * Sends simple request, sets content type to "application/x-www-form-urlencoded"
     * and asks for form fields/files to send in body
     */
    QueryParamStep form();

}
