package projectpackage.service.rateservice;

import projectpackage.model.rates.Rate;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface RateService extends MessageBook{
    public List<Rate> getAllRates();
    public Rate getSingleRateById(Integer id);
    public IUDAnswer insertRate(Rate rate);
}
