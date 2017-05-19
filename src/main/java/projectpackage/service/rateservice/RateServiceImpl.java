package projectpackage.service.rateservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.ratesdao.PriceDAO;
import projectpackage.repository.ratesdao.RateDAO;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

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

    @Autowired
    ReactEAVManager manager;

    @Override
    public List<Rate> getAllRates() {
        return null;
    }

    @Override
    public List<Rate> getAllRates(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Rate getSingleRateById(int id) {
        return null;
    }

    @Override
    public boolean deleteRate(int id) {
//        Rate rate = null;
//        try {
//            rate = (Rate) manager.createReactEAV(Rate.class).
//                    fetchInnerEntityCollection(Price.class).closeFetch().
//                    fetchInnerEntityCollection(RoomType.class).closeFetch().
//                    getSingleEntityWithId(id);
//        } catch (ResultEntityNullException e) {
//            return false;
//        }
        int count = 0;
//        if (null != rate.getPrices()) {
//            for (Price price : rate.getPrices()) {
//                count = count + priceDAO.deletePrice(price.getObjectId());
//            }
//        }
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
            Rate oldRate = (Rate) manager.createReactEAV(Rate.class).
                    fetchInnerEntityCollection(Price.class).closeFetch().
                    getSingleEntityWithId(newRate.getObjectId());
            rateDAO.updateRate(newRate,oldRate);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("Problem with ReactEAV! Pls Check!", e);
            return false;
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
