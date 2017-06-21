package projectpackage.repository.support.daoexceptions;

/**
 * Created by Arizel on 21.06.2017.
 */
public class NullReferenceObjectException extends NullPointerException{
    private static final String DEFAULT_MESSAGE = "The entity has no connection with another entity from ObjReference!" +
            " Entity must has reference for insert or update operation!";

    public NullReferenceObjectException() {
        super(DEFAULT_MESSAGE);
    }
}
