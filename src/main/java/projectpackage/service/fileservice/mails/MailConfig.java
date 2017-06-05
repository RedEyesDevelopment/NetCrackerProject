package projectpackage.service.fileservice.mails;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class MailConfig {
    private String username = null;
    private String password = null;
    private Boolean mailSmtpAuth = true;
    private Boolean mailSmtpStarttlsEnable = true;
    private String mailSmtpHost = "smtp.gmail.com";
    private Integer mailSmtpPort = 587;
    private String mailTransportProtocol = "smtp";

    public MailConfig(String username, String password, Boolean mailSmtpAuth, Boolean mailSmtpStarttlsEnable, String mailSmtpHost, Integer mailSmtpPort, String mailTransportProtocol) {
        this.username = username;
        this.password = password;
        this.mailSmtpAuth = mailSmtpAuth;
        this.mailSmtpStarttlsEnable = mailSmtpStarttlsEnable;
        this.mailSmtpHost = mailSmtpHost;
        this.mailSmtpPort = mailSmtpPort;
        this.mailTransportProtocol = mailTransportProtocol;
    }

    public MailConfig(String username, String password){
        this.username = username;
        this.password = password;
    }
}
