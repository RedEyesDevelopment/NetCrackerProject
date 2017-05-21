package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.service.maintenanceservice.ComplimentaryService;



/**
 * Created by Dmitry on 21.05.2017.
 */
public class ComplimentaryRepositoryTests extends  AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(ComplimentaryRepositoryTests.class);

    @Autowired
    ComplimentaryService complimentaryService;

    @Test
    @Rollback(true)
    public void getAllComplimentaries() {

    }

    @Test
    @Rollback(true)
    public void getSingleComplimentaryById(){

    }


    @Test
    @Rollback(true)
    public void deleteComplimentary(){

    }

    @Test
    @Rollback(true)
    public void createComplimentary(){

    }

    @Test
    @Rollback(true)
    public void updateComplimentary(){

    }
}
