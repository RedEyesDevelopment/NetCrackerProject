package projectpackage.repository.reacteav.exceptions;

/**
 * Created by Lenovo on 12.05.2017.
 */
public class ResultEntityNullException extends RuntimeException {
    private static final String DEFAULTMESSAGE = "Database returned null";

    public ResultEntityNullException() {
        super(DEFAULTMESSAGE);
    }
}
