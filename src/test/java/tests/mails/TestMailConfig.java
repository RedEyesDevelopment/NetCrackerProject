package tests.mails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import projectpackage.service.pdfandmail.*;

/**
 * Created by Gvozd on 06.01.2017.
 */

@Configuration
public class TestMailConfig {

    @Value("YOUR LOGIN")
    private String username;
    @Value("YOUR PASS")
    private String password;


    @Bean
    MailService mailService(){
        return new MailServiceImpl(mailConfig(), mailMessagesMap());
    }

    @Bean
    MailConfig mailConfig(){
        return new MailConfig(username, password);
    }

    @Bean
    MailMessagesMap mailMessagesMap() {
        return new MailMessagesMap();
    }

    @Bean
    JavaMailSender javaMailSender(){
        return new JavaMailSenderImpl();
    }
}
