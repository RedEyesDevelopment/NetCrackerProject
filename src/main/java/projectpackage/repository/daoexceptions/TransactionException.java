package projectpackage.repository.daoexceptions;

import java.io.IOException;

/**
 * Created by Arizel on 15.05.2017.
 */
public class TransactionException extends IOException{
    private static final String DEFAULT_MESSAGE = "Not completed DML operation with entity: ";

    public TransactionException(Object reacEntity) {
        super(DEFAULT_MESSAGE + reacEntity.getClass().getName());
    }
}
