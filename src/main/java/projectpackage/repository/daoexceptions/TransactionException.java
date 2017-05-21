package projectpackage.repository.daoexceptions;

import projectpackage.repository.AbstractDAO;

/**
 * Created by Arizel on 15.05.2017.
 */
public class TransactionException extends Exception{
    private static final String DEFAULT_MESSAGE = "Not completed DML operation with entity: ";

    public TransactionException(AbstractDAO dao) {
        super(DEFAULT_MESSAGE + dao.getClass().getName());
    }
}
