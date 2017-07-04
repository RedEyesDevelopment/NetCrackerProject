package tests.mails;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import projectpackage.service.fileservice.mails.MailService;

import java.io.File;

/**
 * Created by Lenovo on 05.06.2017.
 */
public class MailTests extends AbstractMailTest {

    private static final Logger LOGGER = Logger.getLogger(MailTests.class);

    @Value("denis.yakimov89@gmail.com")
    private String receiver;

    @Autowired
    MailService mailService;

    @Test
    public void sendEmail(){
        LOGGER.info(SEPARATOR);
        mailService.sendEmail(receiver,1);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public void sendEmailWithFile(){
        LOGGER.info(SEPARATOR);
        File file = new File("c:\\temp\\eav.sql");
        mailService.sendEmailWithAttachment(receiver,1,file);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

}
