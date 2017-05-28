package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rates.Rate;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.rateservice.RateService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void crudRateTest() {
        Rate insertRate = new Rate();
        insertRate.setRateFromDate(new Date(16000L));
        insertRate.setRateToDate(new Date(16000L));
        insertRate.setRoomTypeId(8);
        IUDAnswer insertAnswer = rateService.insertRate(insertRate);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Create rate result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int rateId = insertAnswer.getObjectId();
        insertRate.setObjectId(rateId);
        Rate insertedRate = rateService.getSingleRateById(rateId);
        assertEquals(insertRate, insertedRate);

        Rate updateRate = new Rate();
        updateRate.setRateFromDate(new Date(17000L));
        updateRate.setRateToDate(new Date(17000L));
        updateRate.setRoomTypeId(8);
        IUDAnswer updateAnswer = rateService.updateRate(rateId, updateRate);
        assertTrue(updateAnswer.isSuccessful());
        LOGGER.info("Update rate result = " + updateAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Rate updatedRate = rateService.getSingleRateById(rateId);
        assertEquals(updateRate, updatedRate);

        IUDAnswer deleteAnswer = rateService.deleteRate(rateId);
        assertTrue(deleteAnswer.isSuccessful());
        LOGGER.info("Delete rate result = " + deleteAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Rate deletedRate = rateService.getSingleRateById(rateId);
        assertNull(deletedRate);
    }

    @Test
    @Rollback(true)
    public void getAllRates() {
        List<Rate> rates = rateService.getAllRates();
        for (Rate rate : rates) {
            LOGGER.info(rate);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleRateById(){
        Rate rate = rateService.getSingleRateById(5435);
        LOGGER.info(rate);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteRate(){
        int rateId = 2072;
        IUDAnswer iudAnswer = rateService.deleteRate(rateId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete rate result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createRate(){
        Rate rate = new Rate();
        rate.setRateFromDate(new Date());
        rate.setRateToDate(new Date());
        rate.setRoomTypeId(7);
        IUDAnswer iudAnswer = rateService.insertRate(rate);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create rate result = " + iudAnswer.isSuccessful());
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
        rate.setRoomTypeId(8);
        IUDAnswer iudAnswer = rateService.updateRate(2072, rate);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update rate result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
