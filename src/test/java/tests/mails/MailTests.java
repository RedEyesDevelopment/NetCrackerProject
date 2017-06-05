package tests.mails;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import projectpackage.service.pdfandmail.MailService;

/**
 * Created by Lenovo on 05.06.2017.
 */
public class MailTests extends AbstractMailTest {

    @Value("RECEIVER EMAIL")
    private String receiver;

    @Autowired
    MailService mailService;

    @Test
    public void sendEmail(){
        System.out.println(SEPARATOR);
        mailService.sendEmail("plnt9996@mail.ru",0);
    }
}
