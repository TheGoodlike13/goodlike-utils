package eu.goodlike.libraries.spring.gmail;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * <pre>
 * How to set this up in Spring:
 *      1) GMail config details can be found here: https://support.google.com/mail/answer/13287?hl=en (general)
 *      2) Create a @Bean of MailSender
 *          a) import username and password through Environment
 *          b) use GMail.getDefaultSender(username, password)
 *      3) Create a @Bean of GMail
 *      4) Use GMail where you need it
 * </pre>
 */
public final class GMailSender implements GMail {

    @Override
    public void send(String emailTo, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(emailTo);
        message.setSubject(title);
        message.setText(text);
        mailSender.send(message);
    }

    // CONSTRUCTORS

    public GMailSender(String sender, MailSender mailSender) {
        this.sender = sender;
        this.mailSender = mailSender;
    }

    // PRIVATE

    private final String sender;
    private final MailSender mailSender;

}
