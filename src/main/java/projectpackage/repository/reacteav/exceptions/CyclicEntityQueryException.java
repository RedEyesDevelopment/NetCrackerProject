package projectpackage.repository.reacteav.exceptions;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class CyclicEntityQueryException extends RuntimeException {
    private static final String DEFAULTMESSAGE = "Cyclic query has been found! Do not query entity two or more times in one task: ";

    public CyclicEntityQueryException(Class clazz) {
        super(DEFAULTMESSAGE + clazz.getName());
    }
}
