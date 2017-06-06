package tests.mails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import projectpackage.service.fileservice.files.DateFileNameGenerator;
import projectpackage.service.fileservice.files.OrderFileNameGenerator;
import projectpackage.service.fileservice.mails.MailConfig;
import projectpackage.service.fileservice.mails.MailMessagesMap;
import projectpackage.service.fileservice.mails.MailService;
import projectpackage.service.fileservice.mails.MailServiceImpl;

/**
 * Created by Gvozd on 06.01.2017.
 */

@Configuration
public class TestMailConfig {

    @Value("netcrackerhotel17@gmail.com")
    private String username;
    @Value("pkv02zb6l07vkw29z8gjp9jr")
    private String password;


    @Bean
    MailService mailService(){
        return new MailServiceImpl();
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
    DateFileNameGenerator dateFileNameGenerator(){
        return new DateFileNameGenerator("NCHotel");
    }

    @Bean
    OrderFileNameGenerator orderFileNameGenerator(){
        return new OrderFileNameGenerator("NCHotel");
    }
}
