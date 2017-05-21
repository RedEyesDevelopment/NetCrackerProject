package projectpackage.service.rateservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.rates.Price;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.ratesdao.PriceDAO;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class PriceServiceImpl implements PriceService{
    private static final Logger LOGGER = Logger.getLogger(PriceServiceImpl.class);

    @Autowired
    PriceDAO priceDAO;

    @Override
    public List<Price> getAllPrices() {
        List<Price> prices = priceDAO.getAllPrices();
        if (prices == null) LOGGER.info("Returned NULL!!!");
        return prices;
    }

    @Override
    public List<Price> getAllPrices(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Price getSinglePriceById(int id) {
        Price price = priceDAO.getPrice(id);
        if (price == null) LOGGER.info("Returned NULL!!!");
        return price;
    }

    @Override
    public boolean deletePrice(int id) {
        int count = priceDAO.deletePrice(id);
        LOGGER.info("Deleted rows : " + count);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertPrice(Price price) {
        try {
            int priceId = priceDAO.insertPrice(price);
            LOGGER.info("Get from DB phoneId = " + priceId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePrice(int id, Price newPrice) {
        try {
            newPrice.setObjectId(id);
            Price oldPrice = priceDAO.getPrice(id);
            priceDAO.updatePrice(newPrice, oldPrice);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
