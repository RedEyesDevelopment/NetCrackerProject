package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Price;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface PriceDAO {
    public Price getPrice(Integer id);
    public List<Price> getAllPrices();
    public int insertPrice(Price price) throws TransactionException;
    public void updatePrice(Price newPrice, Price oldPrice) throws TransactionException;
    public void deletePrice(int id) throws ReferenceBreakException;
}
