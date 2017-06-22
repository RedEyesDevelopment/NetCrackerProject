package projectpackage.repository.reacteav.exceptions;

public class ResultEntityNullException extends Exception {
    private static final String DEFAULTMESSAGE = "Database returned null";

    public ResultEntityNullException() {
        super(DEFAULTMESSAGE);
    }
}
