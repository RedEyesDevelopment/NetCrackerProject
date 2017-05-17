package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Rate;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RateDAO {
    public int insertRate(Rate rate) throws TransactionException;
    public void updateRate(Rate newRate, Rate oldRate) throws TransactionException;
    public int deleteRate(int id);
}
