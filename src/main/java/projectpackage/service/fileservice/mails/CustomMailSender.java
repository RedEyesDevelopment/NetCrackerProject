package projectpackage.service.fileservice.mails;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import projectpackage.service.fileservice.pdf.PdfService;

import javax.mail.internet.MimeMessage;

public class CustomMailSender implements Runnable {
    private JavaMailSenderImpl javaMailSender;
    private String filePath;
    private PdfService pdfService;
    private Logger log;
    private MimeMessage message;

    public CustomMailSender(PdfService pdfService, String filePath, Logger log, JavaMailSenderImpl javaMailSender, MimeMessage message) {
        this.pdfService = pdfService;
        this.filePath = filePath;
        this.javaMailSender = javaMailSender;
        this.log = log;
        this.message = message;
    }

    private void send(){
        try {
            javaMailSender.send(message);
        } catch (MailException e){
            log.error(e);
        } finally {
            if (null!=pdfService) {
                pdfService.deletePDF(filePath);
            }
        }
    }

    @Override
    public void run() {
        send();
    }
}
