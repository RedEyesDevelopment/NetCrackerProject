package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Price;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class PriceDAOImpl extends AbstractDAO implements PriceDAO{
    @Override
    public int insertPrice(Price price) throws TransactionException {
        return 0;
    }

    @Override
    public void updatePrice(Price newPrice, Price oldPrice) throws TransactionException {

    }

    @Override
    public int deletePrice(int id) {
        return deleteSingleEntityById(id);
    }
}
