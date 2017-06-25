package projectpackage.service.rateservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.rates.Rate;
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
    public Rate getSingleRateById(Integer id) {
        Rate rate = rateDAO.getRate(id);
        if (rate == null) LOGGER.info("Returned NULL!!!");
        return rate;
    }

    @Override
    public IUDAnswer insertRate(Rate rate) {
        Integer rateId = null;
        try {
            rateId = rateDAO.insertRate(rate);
        } catch (IllegalArgumentException e) {
            return rateDAO.rollback(WRONG_FIELD, e);
        }
        rateDAO.commit();
        return new IUDAnswer(rateId,true);
    }
}
