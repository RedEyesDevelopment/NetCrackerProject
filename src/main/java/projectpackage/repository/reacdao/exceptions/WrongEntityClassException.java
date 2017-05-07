package projectpackage.repository.reacdao.exceptions;

/**
 * Created by Lenovo on 06.05.2017.
 */
public class WrongEntityClassException extends RuntimeException {
    private static final String DEFAULTMESSAGE = "Entity class is not an accessor of a ReacEntity abstract class";

    public WrongEntityClassException() {
        super(DEFAULTMESSAGE);
    }

    public WrongEntityClassException(String message) {
        super(DEFAULTMESSAGE + (null != message ? ". " + message : "") + ".");
    }

    public WrongEntityClassException(String message, Class targetClass) {
        super(DEFAULTMESSAGE + (null != message ? ". " + message + "." : (".")) + " Throwed object is " + targetClass.getName().toString());
    }
}