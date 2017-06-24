package projectpackage.repository.ratesdao;

import projectpackage.model.rates.Price;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface PriceDAO {
    public Price getPrice(Integer id);
    public List<Price> getAllPrices();
    public Integer updatePrice(Price newPrice, Price oldPrice);
}
