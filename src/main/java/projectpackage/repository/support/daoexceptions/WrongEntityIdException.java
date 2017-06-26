package projectpackage.repository.support.daoexceptions;

import projectpackage.repository.AbstractDAO;

/**
 * Created by Arizel on 28.05.2017.
 */
public class WrongEntityIdException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Not completed delete operation because entity id belong another entity:"
            + System.getProperty("line.separator");

    public WrongEntityIdException(AbstractDAO dao, String message) {
        super(DEFAULT_MESSAGE + dao.getClass().getName() + System.getProperty("line.separator") + message);
    }
}
