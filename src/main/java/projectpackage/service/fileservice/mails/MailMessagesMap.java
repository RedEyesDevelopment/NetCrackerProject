package projectpackage.service.fileservice.mails;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MailMessagesMap {

    private Map<Integer, MailDataHolder> messages;

    {
        messages = new HashMap<>();
        messages.put(0, new MailDataHolder("Registration", "Hello, you have registered at NCHotel. You can login at our site using this email as login and the password that you have inserted."));
        messages.put(1, new MailDataHolder("Booked room", "You have registered order for a room at NCHotel. Please, pay for your order(order data is attached by a file) and wait for administration of our hotel to contact you."));
        messages.put(2, new MailDataHolder("Payment confirmation", "Thank you for your payment."));
        messages.put(3, new MailDataHolder("Statistic for ", "Statistic for current date."));
        messages.put(4, new MailDataHolder("PasswordChange", "This letter was created automatically, do not reply to this. You have left a request for password change on NetCracker Hotel. We generated dynamic link for you to change your password, it will be active only for 5 minutes. If that's not what you wanted, then do nothing and delete this letter. If that's what you wanted to do, then just click the link below:\n"));
        messages.put(5, new MailDataHolder("PasswordChange", "This letter was created automatically, do not reply to this. You have requested a password change and it was changed. Now you can login using your email and new password. Your new password is: "));
    }

    public MailDataHolder getMessage(int messageCode){
        return messages.get(messageCode);
    }
}
