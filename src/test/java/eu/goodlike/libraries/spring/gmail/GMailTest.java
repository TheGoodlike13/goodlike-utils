package eu.goodlike.libraries.spring.gmail;

import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class GMailTest {

    private final String username = "username";
    private final String password = "password";

    @Test
    public void tryDefaultSender_shouldHaveDefaultParams() {
        JavaMailSenderImpl mailSender = ((JavaMailSenderImpl) GMail.getDefaultSender(username, password));
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        assertThat(mailSender)
                .matches(sender -> sender.getHost().equals("smtp.gmail.com"))
                .matches(sender -> sender.getPort() == 587)
                .matches(sender -> sender.getUsername().equals(username))
                .matches(sender -> sender.getPassword().equals(password))
                .matches(sender -> sender.getJavaMailProperties().equals(javaMailProperties));
    }

}
