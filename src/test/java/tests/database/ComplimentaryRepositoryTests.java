package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.maintenanceservice.ComplimentaryService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    @Rollback
    public void crudComplimentaryTest() {
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(1500);
        maintenance.setMaintenanceType("odezhda");
        maintenance.setMaintenanceTitle("washing");
        maintenance.setMaintenancePrice(300L);
        Complimentary insertComplimentary = new Complimentary();
        insertComplimentary.setCategoryId(32);
        insertComplimentary.setMaintenance(maintenance);
        IUDAnswer insertAnswer = complimentaryService.insertComplimentary(insertComplimentary);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Insert complimentary result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int complimentaryId = insertAnswer.getObjectId();

        Complimentary insertedComplimentary = complimentaryService.getSingleComplimentaryById(complimentaryId);
        insertComplimentary.setObjectId(complimentaryId);
        assertEquals(insertComplimentary, insertedComplimentary);

        Maintenance updateMaintenance = new Maintenance();
        updateMaintenance.setObjectId(1501);
        updateMaintenance.setMaintenancePrice(400L);
        updateMaintenance.setMaintenanceTitle("breakfast");
        updateMaintenance.setMaintenanceType("food");
        Complimentary updateComplimentary = new Complimentary();
        updateComplimentary.setCategoryId(32);
        updateComplimentary.setMaintenance(updateMaintenance);
        IUDAnswer updateAnswer = complimentaryService.updateComplimentary(complimentaryId, updateComplimentary);
        assertTrue(updateAnswer.isSuccessful());
        LOGGER.info("Update complimentary result = " + updateAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Complimentary updatedComplimentary = complimentaryService.getSingleComplimentaryById(complimentaryId);
        assertEquals(updateComplimentary, updatedComplimentary);

        IUDAnswer deleteAnswer = complimentaryService.deleteComplimentary(complimentaryId);
        assertTrue(deleteAnswer.isSuccessful());
        LOGGER.info("Delete complimentary result = " + deleteAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Complimentary deletedComplimentary = complimentaryService.getSingleComplimentaryById(complimentaryId);
        assertNull(deletedComplimentary);
    }

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
