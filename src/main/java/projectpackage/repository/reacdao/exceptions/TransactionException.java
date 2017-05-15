package projectpackage.repository.reacdao.exceptions;

import projectpackage.repository.reacdao.models.ReacEntity;

import java.io.IOException;

/**
 * Created by Arizel on 15.05.2017.
 */
public class TransactionException extends IOException{
    private static final String DEFAULT_MESSAGE = "Not completed DML operation with entity: ";

    public TransactionException(ReacEntity reacEntity) {
        super(DEFAULT_MESSAGE + reacEntity.getClass().getName());
    }
}
