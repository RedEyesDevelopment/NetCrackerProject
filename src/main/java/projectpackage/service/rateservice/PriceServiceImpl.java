package projectpackage.service.rateservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    @Override
    public List<Price> getAllPrices() {
        List<Price> prices = priceDAO.getAllPrices();
        if (prices == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return prices;
    }

    @Transactional(readOnly = true)
    @Override
    public Price getSinglePriceById(Integer id) {
        Price price = priceDAO.getPrice(id);
        if (price == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return price;
    }

    @Transactional
    @Override
    public IUDAnswer updatePrice(Integer id, Price newPrice) {
        if (newPrice == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        newPrice.setObjectId(id);
        Price oldPrice = priceDAO.getPrice(id);
        priceDAO.updatePrice(newPrice, oldPrice);

        return new IUDAnswer(id,true);
    }
}
