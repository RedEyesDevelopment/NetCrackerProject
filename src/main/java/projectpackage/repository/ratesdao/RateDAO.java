package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Rate;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface RateDAO extends Commitable, Rollbackable {
    public Rate getRate(Integer id);
    public List<Rate> getAllRates();
    public Integer insertRate(Rate rate);
    public void deleteRate(Integer id);
}
