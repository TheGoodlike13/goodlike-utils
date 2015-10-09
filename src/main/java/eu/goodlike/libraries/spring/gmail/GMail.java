package eu.goodlike.libraries.spring.gmail;

import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * <pre>
 * Sends an email with GMail
 *
 * Intended for use with Spring
 *
 * Use getDefaultSender() in the @Bean method for convenience
 * </pre>
 */
public interface GMail {

    /**
     * Send an email to given email, using title as subject and text as email body
     */
    void send(String emailTo, String title, String text);

    static MailSender getDefaultSender(String username, String password) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.gmail.com");
        sender.setPort(587);
        sender.setUsername(username);
        sender.setPassword(password);
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        sender.setJavaMailProperties(javaMailProperties);
        return sender;
    }

}
