package projectpackage.repository.reacdao.exceptions;

/**
 * Created by Lenovo on 07.05.2017.
 */
public class WrongTypeClassException extends RuntimeException {
    private static final String DEFAULTMESSAGE = "Entity variable type is not equals to the result type. Check entry type description in entryClass";

    public WrongTypeClassException(Class required, Class actual) {
        super(DEFAULTMESSAGE+"\nRequired variable:"+required.getName()+"\nActual variable(from database):"+actual.getName());
    }
}
