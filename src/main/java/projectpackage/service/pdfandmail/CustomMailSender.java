package projectpackage.service.pdfandmail;

import org.apache.log4j.Priority;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.apache.log4j.Logger;
import javax.mail.internet.MimeMessage;

/**
 * Created by Lenovo on 05.06.2017.
 */
public class CustomMailSender implements Runnable {
    private JavaMailSenderImpl javaMailSender;
    private Logger log;
    private MimeMessage message;

    public CustomMailSender(Logger log, JavaMailSenderImpl javaMailSender, MimeMessage message) {
        this.javaMailSender = javaMailSender;
        this.log = log;
        this.message = message;
    }

    private void send(){
        try {
            javaMailSender.send(message);
        } catch (MailException e){
            log.error(e);
            e.printStackTrace();
        }
        log.log(Priority.INFO, message);
    }

    @Override
    public void run() {
        send();
    }
}
