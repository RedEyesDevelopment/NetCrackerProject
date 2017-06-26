package projectpackage.repository.reacteav.exceptions;

public class ResultEntityNullException extends Exception {
    private static final String DEFAULT_MESSAGE = "Database returned null";

    public ResultEntityNullException() {
        super(DEFAULT_MESSAGE);
    }
}
