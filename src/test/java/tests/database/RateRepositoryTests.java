package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.rates.Rate;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class RateRepositoryTests extends AbstractDatabaseTest{

    @Autowired
    Rate rate;

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

    }

    @Test
    @Rollback(true)
    public void createRate(){

    }

    @Test
    @Rollback(true)
    public void updateRate(){

    }
}
