package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Rate;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RateDAO {
    public Rate getRate(Integer id);
    public List<Rate> getAllRates();
    public int insertRate(Rate rate) throws TransactionException;
    public void updateRate(Rate newRate, Rate oldRate) throws TransactionException;
    public void deleteRate(int id) throws ReferenceBreakException;
}
