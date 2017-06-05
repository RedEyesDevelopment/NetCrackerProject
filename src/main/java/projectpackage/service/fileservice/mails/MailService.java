package projectpackage.service.fileservice.mails;

import java.io.File;

/**
 * Created by Lenovo on 04.06.2017.
 */
public interface MailService {
    public void sendEmail(String receiver, Integer messageKey);
    public void sendEmailWithAttachment(String receiver, Integer messageKey, File attachment);
}
