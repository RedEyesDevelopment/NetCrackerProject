package projectpackage.service.fileservice.mails;

import lombok.Data;

@Data
public class MailDataHolder {
    private String subject;
    private String message;

    public MailDataHolder(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }
}
