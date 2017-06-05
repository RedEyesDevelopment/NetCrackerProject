package projectpackage.service.fileservice.mails;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService{
    private static final Logger LOGGER = Logger.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private MailMessagesMap mailMessagesMap;

    private String username;
    private String password;
    private Properties mailProperties;

    @PostConstruct
    private void init(){
        mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", mailConfig.getMailSmtpAuth());
        mailProperties.put("mail.smtp.starttls.enable", mailConfig.getMailSmtpStarttlsEnable());
        mailProperties.put("mail.smtp.host", mailConfig.getMailSmtpHost());
        mailProperties.put("mail.smtp.port", mailConfig.getMailSmtpPort());
        mailProperties.put("mail.transport.protocol", mailConfig.getMailTransportProtocol());
        username = mailConfig.getUsername();
        password = mailConfig.getPassword();
    }

    private void prepareAndSendMail(Integer messageKey, String receiver, File attributeFile) {
        synchronized (MailServiceImpl.class) {
            JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl) javaMailSender;
            javaMailSenderImpl.setUsername(username);
            javaMailSenderImpl.setPassword(password);
            javaMailSenderImpl.setJavaMailProperties(mailProperties);
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                prepareMail(message,messageKey,receiver,attributeFile);
            } catch (MessagingException e) {
                LOGGER.error(message, e);
            }
            CustomMailSender sender = new CustomMailSender(LOGGER, javaMailSenderImpl,message);
            Thread thread = new Thread(sender);
            thread.start();
        }
    }

    private void prepareMail(MimeMessage message, Integer messageKey, String receiver, File attributeFile) throws MessagingException {
        MimeMessageHelper messageHelper;
        if (null != attributeFile) {
            messageHelper = new MimeMessageHelper(message, true);
        } else messageHelper = new MimeMessageHelper(message);
        messageHelper.setTo(receiver);
        messageHelper.setSubject(mailMessagesMap.getMessage(messageKey).getSubject());
        messageHelper.setSentDate(new Date(System.currentTimeMillis()));
        messageHelper.setText(mailMessagesMap.getMessage(messageKey).getMessage());
        if (messageHelper.isMultipart()) {
            messageHelper.addAttachment(attributeFile.getName(), attributeFile);
        }
    }

    @Override
    public void sendEmail(String receiver, Integer messageKey) {
        prepareAndSendMail(messageKey,receiver,null);
    }

    @Override
    public void sendEmailWithAttachment(String receiver, Integer messageKey, File attributeFile) {
        prepareAndSendMail(messageKey,receiver,attributeFile);
    }
}
