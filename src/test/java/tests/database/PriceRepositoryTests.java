package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.Phone;
import projectpackage.model.rates.Price;
import projectpackage.service.rateservice.PriceService;
import projectpackage.service.rateservice.PriceServiceImpl;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class PriceRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(PriceRepositoryTests.class);

    @Autowired
    PriceService priceService;

    @Test
    @Rollback(true)
    public void getAllPrices() {

    }

    @Test
    @Rollback(true)
    public void getSinglePriceById(){

    }


    @Test
    @Rollback(true)
    public void deletePrice(){
        int priceId = 2032;
        boolean result = priceService.deletePrice(priceId);
        assertTrue(result);
        LOGGER.info("Delete price result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createPrice(){
        Price price = new Price();
        price.setNumberOfPeople(2);
        price.setRate(7897493L);
        price.setRateId(31);
        boolean result = priceService.insertPrice(price);
        assertTrue(result);
        LOGGER.info("Create price result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updatePrice(){
        Price price = new Price();
        price.setNumberOfPeople(1);
        price.setRate(7897494L);
        price.setRateId(32);
        boolean result = priceService.updatePrice(2032, price);
        assertTrue(result);
        LOGGER.info("Update price result = " + result);
        LOGGER.info(SEPARATOR);
    }
}
