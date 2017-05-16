package projectpackage.service.rateservice;

import projectpackage.model.rates.Rate;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RateService {
    public List<Rate> getAllRates();//TODO Pacanu
    public List<Rate> getAllRates(String orderingParameter, boolean ascend);//TODO Pacanu
    public Rate getSingleRateById(int id);//TODO Pacanu
    public boolean deleteRate(Rate rate);//TODO Pacanu
    public boolean insertRate(Rate rate);//TODO Pacanu
    public boolean updateRate(Rate newRate);//TODO Pacanu
}
