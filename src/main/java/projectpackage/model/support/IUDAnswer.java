package projectpackage.model.support;

import lombok.Data;

/**
 * Created by Sergey on 21.05.2017.
 */
@Data
public class IUDAnswer {
    private Integer objectId;
    private boolean successful;
    private String message;

    public IUDAnswer(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public IUDAnswer(Integer objectId, boolean successful, String message) {
        this.objectId = objectId;
        this.successful = successful;
        this.message = message;
    }

    public IUDAnswer(Integer objectId, boolean successful) {
        this.objectId = objectId;
        this.successful = successful;
    }

    public IUDAnswer(boolean successful) {
        this.successful = successful;

    }
}
