package projectpackage.repository.reacdao.exceptions;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class WrongFieldNameException extends RuntimeException{
    private static final String DEFAULTMESSAGE = "No such field in any of the objects, but was mapped in inner entity.";

    public WrongFieldNameException(Class outer, Class inner) {
        super(DEFAULTMESSAGE + "\nOuter class: " + outer.getName() + "\nInner class: " + inner.getName());
    }
}
