package projectpackage.repository.support.daoexceptions;

import projectpackage.repository.AbstractDAO;

/**
 * Created by Arizel on 28.05.2017.
 */
public class DeletedObjectNotExistsException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Not completed delete operation because entity with that id doesn't exist:"
            + System.getProperty("line.separator");

    public DeletedObjectNotExistsException(AbstractDAO dao) {
        super(DEFAULT_MESSAGE + dao.getClass().getName() + System.getProperty("line.separator"));
    }
}
