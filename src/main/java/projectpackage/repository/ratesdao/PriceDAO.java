package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Price;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface PriceDAO {
    public int insertPrice(Price price) throws TransactionException;
    public void updatePrice(Price newPrice, Price oldPrice) throws TransactionException;
}
