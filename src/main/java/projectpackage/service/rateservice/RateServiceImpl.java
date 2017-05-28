package projectpackage.service.rateservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.rates.Rate;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.ratesdao.RateDAO;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class RateServiceImpl implements RateService{
    private static final Logger LOGGER = Logger.getLogger(RateServiceImpl.class);

    @Autowired
    RateDAO rateDAO;

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
    public IUDAnswer deleteRate(int id) {
        try {
            rateDAO.deleteRate(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(id,false, e.printReferencesEntities());
        }
        return new IUDAnswer(id,true);
    }

    @Override
    public IUDAnswer insertRate(Rate rate) {
        Integer rateId = null;
        try {
            rateId = rateDAO.insertRate(rate);
            LOGGER.info("Get from DB rateId = " + rateId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(rateId,false, "transactionInterrupt");
        }
        return new IUDAnswer(rateId,true);
    }

    @Override
    public IUDAnswer updateRate(int id, Rate newRate) {
        try {
            newRate.setObjectId(id);
            Rate oldRate = rateDAO.getRate(id);
            rateDAO.updateRate(newRate, oldRate);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, "transactionInterrupt");
        }
        return new IUDAnswer(id,true);
    }
}
