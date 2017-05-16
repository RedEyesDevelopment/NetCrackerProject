package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.Phone;
import projectpackage.model.rates.Price;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class PriceRepositoryTests extends AbstractDatabaseTest{

    @Autowired
    Price price;

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

    }

    @Test
    @Rollback(true)
    public void createPrice(){

    }

    @Test
    @Rollback(true)
    public void updatePrice(){

    }
}
