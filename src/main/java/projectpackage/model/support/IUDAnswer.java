package projectpackage.model.support;

import lombok.Data;

/**
 * Created by Sergey on 21.05.2017.
 */
@Data
public class IUDAnswer {

    private boolean successful;
    private String message;

    public IUDAnswer(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public IUDAnswer(boolean successful) {
        this.successful = successful;

    }
}
