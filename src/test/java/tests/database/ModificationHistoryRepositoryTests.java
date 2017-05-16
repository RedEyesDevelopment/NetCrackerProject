package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.service.orderservice.ModificationHistoryService;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class ModificationHistoryRepositoryTests extends AbstractDatabaseTest{

    @Autowired
    ModificationHistoryService modificationHistoryService;

    @Test
    @Rollback(true)
    public void getAllModificationHistory() {

    }

    @Test
    @Rollback(true)
    public void getAllModificationHistoryByOrder(){

    }

    @Test
    @Rollback(true)
    public void getSingleModificationHistoryById(){

    }
}
