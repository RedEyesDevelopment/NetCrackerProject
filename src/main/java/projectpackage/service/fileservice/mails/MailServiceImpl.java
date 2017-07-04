package projectpackage.service.fileservice.mails;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.service.fileservice.pdf.PdfService;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = Logger.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private MailMessagesMap mailMessagesMap;

    private String username;
    private String password;
    private Properties mailProperties;

    @PostConstruct
    private void init() {
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
            MimeMessageHelper messageHelper;
            try {
                if (null != attributeFile) {
                    messageHelper = new MimeMessageHelper(message, true);
                } else {
                    messageHelper = new MimeMessageHelper(message);
                }
                messageHelper.setTo(receiver);
                messageHelper.setSubject(mailMessagesMap.getMessage(messageKey).getSubject());
                messageHelper.setSentDate(new Date(System.currentTimeMillis()));
                messageHelper.setText(mailMessagesMap.getMessage(messageKey).getMessage());
                if (messageHelper.isMultipart()) {
                    messageHelper.addAttachment(attributeFile.getName(), attributeFile);
                }
            } catch (MessagingException e) {
                LOGGER.warn("Message from MailSenderImpl was not send.", e);
            }
            CustomMailSender sender = new CustomMailSender(pdfService, attributeFile.getPath(), LOGGER, javaMailSenderImpl, message);
            Thread thread = new Thread(sender);
            thread.start();
        }
    }

    @Override
    public void sendEmail(String receiver, Integer messageKey) {
        prepareAndSendMail(messageKey, receiver, null);
    }

    @Override
    public void sendEmailWithAttachment(String receiver, Integer messageKey, File attributeFile) {
        prepareAndSendMail(messageKey, receiver, attributeFile);
    }

    @Override
    public void sendEmailToMyself(User client, String about, String message) {
        synchronized (MailServiceImpl.class) {
            JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl) javaMailSender;
            javaMailSenderImpl.setUsername(username);
            javaMailSenderImpl.setPassword(password);
            javaMailSenderImpl.setJavaMailProperties(mailProperties);
            MimeMessage messageData = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(messageData);
            try {
                messageHelper.setTo(username);
                messageHelper.setSubject(about);
                messageHelper.setSentDate(new Date(System.currentTimeMillis()));
                StringBuilder messageBuilder = new StringBuilder("Message from user: ").append(client.getFirstName()).append(" ").append(client.getLastName()).append(" with userId=").append(client.getObjectId()).append('\n').append(message);
                messageHelper.setText(messageBuilder.toString());
                CustomMailSender sender = new CustomMailSender(null, null, LOGGER, javaMailSenderImpl, messageData);
                Thread thread = new Thread(sender);
                thread.start();
            } catch (MessagingException e) {
                LOGGER.warn("Message from MailSenderImpl was not send.", e);
            }
        }
    }

    @Override
    public void sendEmailToChangePassword(String receiver, Integer messageKey, String link) {
        synchronized (MailServiceImpl.class) {
            JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl) javaMailSender;
            javaMailSenderImpl.setUsername(username);
            javaMailSenderImpl.setPassword(password);
            javaMailSenderImpl.setJavaMailProperties(mailProperties);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            try {
                messageHelper.setTo(receiver);
                messageHelper.setSubject(mailMessagesMap.getMessage(messageKey).getSubject());
                messageHelper.setSentDate(new Date(System.currentTimeMillis()));
                String stringMessage = mailMessagesMap.getMessage(messageKey).getMessage() + link;
                messageHelper.setText(stringMessage);
            } catch (MessagingException e) {
                LOGGER.warn("Message from MailSenderImpl was not send.", e);
            }
            CustomMailSender sender = new CustomMailSender(null, null, LOGGER, javaMailSenderImpl, message);
            Thread thread = new Thread(sender);
            thread.start();
        }
    }

    @Override
    public void sendEmailForStatistics(String receiver, String dates, File attributeFile) {
        synchronized (MailServiceImpl.class) {
            JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl) javaMailSender;
            javaMailSenderImpl.setUsername(username);
            javaMailSenderImpl.setPassword(password);
            javaMailSenderImpl.setJavaMailProperties(mailProperties);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = null;
            try {
                messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setTo(receiver);
                messageHelper.setSubject(mailMessagesMap.getMessage(3).getSubject() + dates);
                messageHelper.setSentDate(new Date(System.currentTimeMillis()));
                messageHelper.setText(mailMessagesMap.getMessage(3).getMessage());
                messageHelper.addAttachment(attributeFile.getName(), attributeFile);
            } catch (MessagingException e) {
                LOGGER.error(e);
            }
            CustomMailSender sender = new CustomMailSender(pdfService, attributeFile.getPath(), LOGGER, javaMailSenderImpl, message);
            Thread thread = new Thread(sender);
            thread.start();
        }
    }
}
