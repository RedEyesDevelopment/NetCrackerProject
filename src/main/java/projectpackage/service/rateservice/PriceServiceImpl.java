package projectpackage.service.rateservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.rates.Price;
import projectpackage.repository.ratesdao.PriceDAO;

import java.util.List;

@Log4j
@Service
public class PriceServiceImpl implements PriceService{
    private static final Logger LOGGER = Logger.getLogger(PriceServiceImpl.class);

    @Autowired
    PriceDAO priceDAO;

    @Override
    public List<Price> getAllPrices() {
        List<Price> prices = priceDAO.getAllPrices();
        if (prices == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return prices;
    }

    @Override
    public Price getSinglePriceById(Integer id) {
        Price price = priceDAO.getPrice(id);
        if (price == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return price;
    }

    @Override
    public IUDAnswer updatePrice(Integer id, Price newPrice) {
        if (newPrice == null) {
            return null;
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        try {
            newPrice.setObjectId(id);
            Price oldPrice = priceDAO.getPrice(id);
            priceDAO.updatePrice(newPrice, oldPrice);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id,false, WRONG_FIELD, e.getMessage());
        }
        priceDAO.commit();
        return new IUDAnswer(id,true);
    }
}
