package projectpackage.service.rateservice;

import projectpackage.model.rates.Price;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface PriceService {
    public List<Price> getAllPrices();//TODO Pacanu
    public List<Price> getAllPrices(String orderingParameter, boolean ascend);//TODO Pacanu
    public Price getSinglePriceById(int id);//TODO Pacanu
    public boolean deletePrice(int id);//TODO Pacanu
    public boolean insertPrice(Price price);//TODO Pacanu
    public boolean updatePrice(int id, Price newPrice);//TODO Pacanu
}
