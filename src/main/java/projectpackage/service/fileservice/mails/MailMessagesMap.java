package projectpackage.service.fileservice.mails;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MailMessagesMap {

    private Map<Integer, MailDataHolder> messages;

    {
        messages = new HashMap<>();
        messages.put(0, new MailDataHolder("Registration", "User creation confirmation"));
        messages.put(1, new MailDataHolder("Booked room", "Order creation"));
        messages.put(2, new MailDataHolder("Payment confirmation", "Order has been payed"));
        messages.put(3, new MailDataHolder("Statistic", "Fuck you bitch!"));
    }

    public MailDataHolder getMessage(int messageCode){
        return messages.get(messageCode);
    }
}
