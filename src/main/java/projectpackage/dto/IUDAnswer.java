package projectpackage.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergey on 21.05.2017.
 */
@Data
public class IUDAnswer {
    private static transient final Map<String, String> ANSWERS = new HashMap<>();

    static {
        ANSWERS.put("wrongId", "Cannot update with wrong id! Updated entity id not equals original entity id, but id cannot be changed!");
        ANSWERS.put("wrongDeleteId", "Cannot delete entity with inadequate id! Entity id does not match entity class! Check your request!");
        ANSWERS.put("deletedObjectNotExists", "Cannot execute delete operation because deleted entity does not exists! Check your request!");
        ANSWERS.put("wrongPhoneNumber", "Cannot insert or update incorrect phone number!");
        ANSWERS.put("transactionInterrupt", "The transaction was aborted from add operation!");
        ANSWERS.put("emptyRoomNotFound", "The room of this type is not available on current date.");
        ANSWERS.put("orderCreated", "The order has been confirmed. The message with requisites was sent to your email.");
        ANSWERS.put("orderCanceled", "The order has been canceled.");
        ANSWERS.put("duplicateEmail", "This email is already exist!");
        ANSWERS.put("statWrong", "Unable to create statistics!");
        ANSWERS.put("failedEmailSent", "Your message haven't been sent!");
    }

    private Integer objectId;
    private boolean successful;
    private String message;

    public IUDAnswer(boolean successful) {
        this.successful = successful;
    }

    public IUDAnswer(Integer objectId, boolean successful) {
        this.objectId = objectId;
        this.successful = successful;
    }

    public IUDAnswer(Integer objectId, boolean successful, String message) {
        this.objectId = objectId;
        this.successful = successful;
        this.message = IUDAnswer.ANSWERS.get(message);
    }

    public IUDAnswer(Integer objectId, String messageKey) {
        this.objectId = objectId;
        this.successful = false;
        this.message = IUDAnswer.ANSWERS.get(messageKey);
    }

    public IUDAnswer(boolean successful, String messageKey) {
        this.successful = successful;
        this.message = IUDAnswer.ANSWERS.get(messageKey);
    }
}
