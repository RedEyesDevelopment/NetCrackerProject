package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Rate;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RateDAO {
    public Rate getRate(Integer id);
    public List<Rate> getAllRates();
    public Integer insertRate(Rate rate) throws TransactionException;
    public void deleteRate(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
