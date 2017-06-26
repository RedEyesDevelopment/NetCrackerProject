package projectpackage.repository.reacteav.exceptions;

public class WrongEntityClassException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Entity class is not an accessor of a ReacEntity abstract class";

    public WrongEntityClassException() {
        super(DEFAULT_MESSAGE);
    }

    public WrongEntityClassException(String message) {
        super(DEFAULT_MESSAGE + (null != message ? ". " + message : "") + ".");
    }

    public WrongEntityClassException(String message, Class targetClass) {
        super(DEFAULT_MESSAGE + (null != message ? ". " + message + "." : (".")) + " Throwed object is " + targetClass.getName());
    }
}