package projectpackage.repository.reacdao.exceptions;

/**
 * Created by Lenovo on 12.05.2017.
 */
public class ResultEntityNullException extends Exception{
    private static final String DEFAULTMESSAGE = "Database returned null";

    public ResultEntityNullException() {
        super(DEFAULTMESSAGE);
    }
}
