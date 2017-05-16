package projectpackage.service.rateservice;

import projectpackage.model.rates.Rate;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RateServiceImpl implements RateService{
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
        return false;
    }

    @Override
    public boolean insertRate(Rate rate) {
        return false;
    }

    @Override
    public boolean updateRate(Rate newRate) {
        return false;
    }
}
