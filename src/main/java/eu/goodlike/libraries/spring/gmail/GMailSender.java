package eu.goodlike.libraries.spring.gmail;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

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
