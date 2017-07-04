package projectpackage.service.rateservice;

import projectpackage.model.rates.Price;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface PriceService extends MessageBook{
    public List<Price> getAllPrices();
    public Price getSinglePriceById(Integer id);
    public IUDAnswer updatePrice(Integer id, Price newPrice);
}
