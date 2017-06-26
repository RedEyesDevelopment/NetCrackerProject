package projectpackage.repository.reacteav.exceptions;

public class WrongTypeClassException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Entity variable type is not equals to the result type. Check entry type description in entryClass";

    public WrongTypeClassException(Class required, Class actual) {
        super(DEFAULT_MESSAGE + "\nRequired variable:" + required.getName() + "\nActual variable(from database):" + actual.getName());
    }
}
