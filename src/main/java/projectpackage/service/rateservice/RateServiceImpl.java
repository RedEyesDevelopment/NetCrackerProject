package projectpackage.service.rateservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.rates.Rate;
import projectpackage.repository.ratesdao.RateDAO;
import projectpackage.service.support.ServiceUtils;

import java.util.List;

@Log4j
@Service
public class RateServiceImpl implements RateService{
    private static final Logger LOGGER = Logger.getLogger(RateServiceImpl.class);

    @Autowired
    RateDAO rateDAO;

    @Autowired
    ServiceUtils serviceUtils;

    @Transactional(readOnly = true)
    @Override
    public List<Rate> getAllRates() {
        List<Rate> rates = rateDAO.getAllRates();
        if (rates == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return rates;
    }

    @Transactional(readOnly = true)
    @Override
    public Rate getSingleRateById(Integer id) {
        Rate rate = rateDAO.getRate(id);
        if (rate == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return rate;
    }

    @Transactional
    @Override
    public IUDAnswer insertRate(Rate rate) {
        if (rate == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        } else if (!serviceUtils.checkDatesForRate(rate.getRateFromDate(), rate.getRateToDate())) {
            return new IUDAnswer(false, WRONG_RATE_DATES);
        }
        Integer rateId = rateDAO.insertRate(rate);

        return new IUDAnswer(rateId,true);
    }
}
