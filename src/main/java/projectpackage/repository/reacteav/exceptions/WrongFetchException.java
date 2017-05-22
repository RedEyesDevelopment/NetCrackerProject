package projectpackage.repository.reacteav.exceptions;

/**
 * Created by Lenovo on 21.05.2017.
 */
public class WrongFetchException extends RuntimeException{
    private static final String DEFAULTMESSAGE = "No such inner class in object.";

    public WrongFetchException(Class outer, Class inner, String task) {
        super(DEFAULTMESSAGE + " Task="+task +"\nOuter class: " + outer.getName() + "\nInner class: " + inner.getName());
    }
}
