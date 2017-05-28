package projectpackage.repository.daoexceptions;

import projectpackage.repository.AbstractDAO;

/**
 * Created by Arizel on 28.05.2017.
 */
public class WrongIdException extends Exception{
    private static final String DEFAULT_MESSAGE = "Not completed delete operation because entity with that id doesn't exist:"
            + System.getProperty("line.separator");

    public WrongIdException(AbstractDAO dao) {
        super(DEFAULT_MESSAGE + dao.getClass().getName() + System.getProperty("line.separator"));
    }
}
