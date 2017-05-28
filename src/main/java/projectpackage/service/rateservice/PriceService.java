package projectpackage.service.rateservice;

import projectpackage.model.rates.Price;
import projectpackage.model.support.IUDAnswer;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface PriceService {
    public List<Price> getAllPrices();
    public List<Price> getAllPrices(String orderingParameter, boolean ascend);
    public Price getSinglePriceById(int id);
    public IUDAnswer deletePrice(int id);
    public IUDAnswer insertPrice(Price price);
    public IUDAnswer updatePrice(int id, Price newPrice);
}
