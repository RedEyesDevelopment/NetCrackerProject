package tests.mails;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import projectpackage.service.fileservice.mails.MailService;

import java.io.File;

/**
 * Created by Lenovo on 05.06.2017.
 */
public class MailTests extends AbstractMailTest {

    @Value("denis.yakimov89@gmail.com")
    private String receiver;

    @Autowired
    MailService mailService;

    @Test
    public void sendEmail(){
        System.out.println(SEPARATOR);
        mailService.sendEmail(receiver,1);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendEmailWithFile(){
        System.out.println(SEPARATOR);
        File file = new File("c:\\temp\\eav.sql");
        mailService.sendEmailWithAttachment(receiver,1,file);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
