package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.maintenanceservice.ComplimentaryService;

import java.util.List;

import static org.junit.Assert.assertTrue;


/**
 * Created by Dmitry on 21.05.2017.
 */
@Log4j
public class ComplimentaryRepositoryTests extends  AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(ComplimentaryRepositoryTests.class);

    @Autowired
    ComplimentaryService complimentaryService;

    @Test
    @Rollback(true)
    public void getAllComplimentaries() {
        List<Complimentary> complimentaries = complimentaryService.getAllComplimentaries();
        for (Complimentary c : complimentaries) {
            LOGGER.info(c);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleComplimentaryById(){
        Complimentary complimentary = complimentaryService.getSingleComplimentaryById(4223);//check id
        LOGGER.info(complimentary);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteComplimentary(){
        int complimentaryId = 2051;
        IUDAnswer iudAnswer = complimentaryService.deleteComplimentary(complimentaryId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete complimentary result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createComplimentary(){
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(1500);
        Complimentary complimentary = new Complimentary();
        complimentary.setCategoryId(32);
        complimentary.setMaintenance(maintenance);
        IUDAnswer iudAnswer = complimentaryService.insertComplimentary(complimentary);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Insert complimentary result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateComplimentary(){
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(1501);
        Complimentary complimentary = new Complimentary();
        complimentary.setCategoryId(32);
        complimentary.setMaintenance(maintenance);
        IUDAnswer iudAnswer = complimentaryService.updateComplimentary(2051, complimentary);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update complimentary result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
