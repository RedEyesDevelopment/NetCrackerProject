package projectpackage.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import projectpackage.service.fileservice.mails.MailConfig;
import projectpackage.service.fileservice.mails.MailMessagesMap;

@Configuration
public class MailConfiguration {

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

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
