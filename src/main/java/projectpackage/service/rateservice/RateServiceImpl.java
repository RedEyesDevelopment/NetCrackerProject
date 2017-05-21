package projectpackage.service.rateservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.ratesdao.PriceDAO;
import projectpackage.repository.ratesdao.RateDAO;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RateServiceImpl implements RateService{
    private static final Logger LOGGER = Logger.getLogger(RateServiceImpl.class);

    @Autowired
    RateDAO rateDAO;

    @Autowired
    PriceDAO priceDAO;

    @Override
    public List<Rate> getAllRates() {
        List<Rate> rates = rateDAO.getAllRates();
        if (rates == null) LOGGER.info("Returned NULL!!!");
        return rates;
    }

    @Override
    public List<Rate> getAllRates(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Rate getSingleRateById(int id) {
        Rate rate = rateDAO.getRate(id);
        if (rate == null) LOGGER.info("Returned NULL!!!");
        return rate;
    }

    @Override
    public boolean deleteRate(int id) {
        Rate rate = rateDAO.getRate(id);
        for (Price price : rate.getPrices()) {
            priceDAO.deletePrice(id);
        }
        int count = 0;
        count = count + rateDAO.deleteRate(id);
        if (count == 0) return false;
        else return true;
    }

    @Override
    public boolean insertRate(Rate rate) {
        try {
            int rateId = rateDAO.insertRate(rate);
            LOGGER.info("Get from DB rateId = " + rateId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateRate(int id, Rate newRate) {
        try {
            newRate.setObjectId(id);
            Rate oldRate = rateDAO.getRate(id);
            rateDAO.updateRate(newRate, oldRate);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
