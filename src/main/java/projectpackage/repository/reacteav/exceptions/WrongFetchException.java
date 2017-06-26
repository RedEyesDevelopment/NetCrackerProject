package projectpackage.repository.reacteav.exceptions;

public class WrongFetchException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "No such inner class in object.";

    public WrongFetchException(Class outer, Class inner, String task) {
        super(DEFAULT_MESSAGE + " Task="+task +"\nOuter class: " + outer.getName() + "\nInner class: " + inner.getName());
    }
}
