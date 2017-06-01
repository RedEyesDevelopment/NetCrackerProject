package projectpackage.repository.support.daoexceptions;

import projectpackage.repository.AbstractDAO;

/**
 * Created by Arizel on 15.05.2017.
 */
public class TransactionException extends Exception{
    private static final String DEFAULT_MESSAGE = "Not completed DML operation with entity: "
            + System.getProperty("line.separator");

    public TransactionException(AbstractDAO dao, String message) {
        super(DEFAULT_MESSAGE + dao.getClass().getName() + System.getProperty("line.separator") + message);
    }
}
