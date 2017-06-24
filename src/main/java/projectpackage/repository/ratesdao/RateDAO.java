package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Rate;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RateDAO {
    public Rate getRate(Integer id);
    public List<Rate> getAllRates();
    public Integer insertRate(Rate rate);
    public void deleteRate(Integer id);
}
