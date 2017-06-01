package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rates.Price;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.rateservice.PriceService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class PriceRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(PriceRepositoryTests.class);

    @Autowired
    PriceService priceService;

//    @Test
//    @Rollback(true)
//    public void crudPriceTest() {
//        Price insertPrice = new Price();
//        insertPrice.setNumberOfPeople(2);
//        insertPrice.setRate(7897493L);
//        insertPrice.setRateId(32);
//        IUDAnswer insertAnswer = priceService.insertPrice(insertPrice);
//        assertTrue(insertAnswer.isSuccessful());
//        LOGGER.info("Create price result = " + insertAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//
//        int priceId = insertAnswer.getObjectId();
//        Price insertedPrice = priceService.getSinglePriceById(priceId);
//        insertPrice.setObjectId(priceId);
//        assertEquals(insertPrice, insertedPrice);
//
//        Price updatePrice = new Price();
//        updatePrice.setNumberOfPeople(1);
//        updatePrice.setRate(7897494L);
//        updatePrice.setRateId(32);
//        IUDAnswer updateAnswer = priceService.updatePrice(priceId, updatePrice);
//        assertTrue(updateAnswer.isSuccessful());
//        LOGGER.info("Update price result = " + updateAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//
//        Price updatedPrice = priceService.getSinglePriceById(priceId);
//        assertEquals(updatePrice, updatedPrice);
//
//        IUDAnswer deleteAnswer = priceService.deletePrice(priceId);
//        assertTrue(deleteAnswer.isSuccessful());
//        LOGGER.info("Delete price result = " + deleteAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//
//        Price deletedPrice = priceService.getSinglePriceById(priceId);
//        assertNull(deletedPrice);
//    }

    @Test
    @Rollback(true)
    public void getAllPrices() {
        List<Price> prices = priceService.getAllPrices();
        for (Price price : prices) {
            LOGGER.info(price);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSinglePriceById(){
        Price price = priceService.getSinglePriceById(453);
        LOGGER.info(price);
        LOGGER.info(SEPARATOR);
    }


//    @Test
//    @Rollback(true)
//    public void deletePrice(){
//        int priceId = 2071;
//        IUDAnswer iudAnswer = priceService.deletePrice(priceId);
//        assertTrue(iudAnswer.isSuccessful());
//        LOGGER.info("Delete price result = " + iudAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//    }

//    @Test
//    @Rollback(true)
//    public void createPrice(){
//        Price price = new Price();
//        price.setNumberOfPeople(2);
//        price.setRate(7897493L);
//        price.setRateId(31);
//        IUDAnswer iudAnswer = priceService.insertPrice(price);
//        assertTrue(iudAnswer.isSuccessful());
//        LOGGER.info("Create price result = " + iudAnswer.isSuccessful());
//        LOGGER.info(SEPARATOR);
//    }

    @Test
    @Rollback(true)
    public void updatePrice(){
        Price price = new Price();
        price.setNumberOfPeople(1);
        price.setRate(7897494L);
        price.setRateId(32);
        IUDAnswer iudAnswer = priceService.updatePrice(2071, price);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update price result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
