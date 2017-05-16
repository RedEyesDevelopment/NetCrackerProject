package projectpackage.service.rateservice;

import projectpackage.model.rates.Price;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class PriceServiceImpl implements PriceService{
    @Override
    public List<Price> getAllPrices() {
        return null;
    }

    @Override
    public List<Price> getAllPrices(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public Price getSinglePriceById(int id) {
        return null;
    }

    @Override
    public boolean deletePrice(int id) {
        return false;
    }

    @Override
    public boolean insertPrice(Price price) {
        return false;
    }

    @Override
    public boolean updatePrice(int id, Price newPrice) {
        return false;
    }
}
