package projectpackage.service.fileservice.mails;

import projectpackage.model.auth.User;

import java.io.File;

public interface MailService {
    public void sendEmail(String receiver, Integer messageKey);
    public void sendEmailWithAttachment(String receiver, Integer messageKey, File attachment);
    public void sendEmailToMyself(User client, String about, String message);
    public void sendEmailToChangePassword(String receiver, Integer messageKey, String link);
    public void sendEmailForStatistics(String receiver, String dates, File attributeFile);
}
