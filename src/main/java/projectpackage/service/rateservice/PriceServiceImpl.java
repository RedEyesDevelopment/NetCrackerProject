package projectpackage.service.rateservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.rates.Price;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.ratesdao.PriceDAO;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
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
    public IUDAnswer deletePrice(int id) {
        try {
            priceDAO.deletePrice(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(id,false, e.printReferencesEntities());
        }
        return new IUDAnswer(id,true);
    }

    @Override
    public IUDAnswer insertPrice(Price price) {
        Integer priceId = null;
        try {
            priceId = priceDAO.insertPrice(price);
            LOGGER.info("Get from DB phoneId = " + priceId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(priceId,false, e.getMessage());
        }
        return new IUDAnswer(priceId,true);
    }

    @Override
    public IUDAnswer updatePrice(int id, Price newPrice) {
        try {
            newPrice.setObjectId(id);
            Price oldPrice = priceDAO.getPrice(id);
            priceDAO.updatePrice(newPrice, oldPrice);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, e.getMessage());
        }
        return new IUDAnswer(id,true);
    }
}
