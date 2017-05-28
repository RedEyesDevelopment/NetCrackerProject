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
        ANSWERS.put("wrongDelete", "Cannot delete entity with inadequate id! Entity id does not match entity class! Check your request!");
    }

    private Integer objectId;
    private boolean successful;
    private String message;

    public IUDAnswer(Integer objectId, boolean successful) {
        this.objectId = objectId;
        this.successful = successful;
    }

    public IUDAnswer(Integer objectId, String messageKey) {
        this.objectId = objectId;
        this.successful = false;
        this.message = IUDAnswer.ANSWERS.get(messageKey);
    }
}
