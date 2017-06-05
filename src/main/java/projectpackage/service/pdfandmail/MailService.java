package projectpackage.service.pdfandmail;

import java.io.File;

/**
 * Created by Lenovo on 04.06.2017.
 */
public interface MailService {
    public void sendEmail(String receiver, Integer messageKey);
    public void sendEmailWithAttachment(String receiver, Integer messageKey, File attachment);
}
