package eu.goodlike.gmail;

/**
 * <pre>
 * Sends an email with GMail
 *
 * Intended for use with Spring
 * </pre>
 */
public interface GMail {

    /**
     * Send an email to given email, using title as subject and text as email body
     */
    void send(String emailTo, String title, String text);

}
