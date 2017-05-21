package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rates.Rate;
import projectpackage.service.rateservice.RateService;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RateRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(RateRepositoryTests.class);

    @Autowired
    RateService rateService;

    @Test
    @Rollback(true)
    public void getAllRates() {

    }

    @Test
    @Rollback(true)
    public void getSingleRateById(){

    }


    @Test
    @Rollback(true)
    public void deleteRate(){
        int rateId = 2036;
        boolean result = rateService.deleteRate(rateId);
        assertTrue(result);
        LOGGER.info("Delete rate result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createRate(){
        Rate rate = new Rate();
        rate.setRateFromDate(new Date());
        rate.setRateToDate(new Date());
//        rate.setCreationDate(new Date());
        rate.setRoomTypeId(7);
        boolean result = rateService.insertRate(rate);
        assertTrue(result);
        LOGGER.info("Create rate result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateRate(){
        Calendar cal = Calendar.getInstance();
        cal.set(2012, Calendar.JANUARY, 1);
        Date date = cal.getTime();
        Rate rate = new Rate();
        rate.setRateFromDate(new Date());
        rate.setRateToDate(new Date());
//        rate.setCreationDate(date);
        rate.setRoomTypeId(8);
        boolean result = rateService.updateRate(2036, rate);
        assertTrue(result);
        LOGGER.info("Update rate result = " + result);
        LOGGER.info(SEPARATOR);
    }
}
