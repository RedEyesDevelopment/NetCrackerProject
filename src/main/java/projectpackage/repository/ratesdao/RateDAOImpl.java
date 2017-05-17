package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Rate;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RateDAOImpl extends AbstractDAO implements RateDAO{
    @Override
    public int insertRate(Rate rate) throws TransactionException {
        return 0;
    }

    @Override
    public void updateRate(Rate newRate, Rate oldRate) throws TransactionException {

    }
}
