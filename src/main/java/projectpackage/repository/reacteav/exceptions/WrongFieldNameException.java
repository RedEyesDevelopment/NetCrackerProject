package projectpackage.repository.reacteav.exceptions;

public class WrongFieldNameException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "No such field in any of the objects, but was mapped in inner entity.";

    public WrongFieldNameException(Class outer, Class inner) {
        super(DEFAULT_MESSAGE + "\nOuter class: " + outer.getName() + "\nInner class: " + inner.getName());
    }
}
