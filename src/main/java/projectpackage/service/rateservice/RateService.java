package projectpackage.service.rateservice;

import projectpackage.model.rates.Rate;
import projectpackage.dto.IUDAnswer;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RateService {
    public List<Rate> getAllRates();
    public List<Rate> getAllRates(String orderingParameter, boolean ascend);
    public Rate getSingleRateById(int id);
    public IUDAnswer insertRate(Rate rate);
//    public IUDAnswer updateRate(int id, Rate newRate);
}
