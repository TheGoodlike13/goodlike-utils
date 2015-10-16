package eu.goodlike.libraries.spring.gmail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class GMailSenderTest {

    private final String sender = "me";
    private final String emailTo = "you";
    private final String title = "this";
    private final String text ="is a test";

    private SimpleMailMessage message;
    private MailSender mailSender;
    private GMailSender gMailSender;

    @Before
    public void setup() {
        message = new SimpleMailMessage();
        mailSender = Mockito.mock(MailSender.class);
        gMailSender = new GMailSender(sender, mailSender);
    }

    @Test
    public void trySendingMail_shouldSendMail() {
        message.setFrom(sender);
        message.setTo(emailTo);
        message.setSubject(title);
        message.setText(text);

        Mockito.doNothing().when(mailSender).send(message);

        gMailSender.send(emailTo, title, text);

        Mockito.verify(mailSender, Mockito.times(1)).send(message);
    }

}
