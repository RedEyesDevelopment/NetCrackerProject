package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.maintenanceservice.MaintenanceService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 22.05.2017.
 */
@Log4j
public class MaintenanceRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(MaintenanceRepositoryTests.class);

    @Autowired
    MaintenanceService maintenanceService;

    @Test
    @Rollback
    public void crudMaintenanceTest() {
        Maintenance insertMaintenance = new Maintenance();
        insertMaintenance.setMaintenancePrice(534L);
        insertMaintenance.setMaintenanceTitle("title main");
        insertMaintenance.setMaintenanceType("type of main");
        IUDAnswer insertAnswer = maintenanceService.insertMaintenance(insertMaintenance);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Insert maintenance result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int maintenanceId = insertAnswer.getObjectId();
        Maintenance insertedMaintenance = maintenanceService.getSingleMaintenanceById(maintenanceId);
        insertMaintenance.setObjectId(maintenanceId);
        assertEquals(insertMaintenance, insertedMaintenance);

        Maintenance updateMaintenance = new Maintenance();
        updateMaintenance.setMaintenancePrice(600L);
        updateMaintenance.setMaintenanceTitle("new title main");
        updateMaintenance.setMaintenanceType("new type of main");
        IUDAnswer updateAnswer = maintenanceService.updateMaintenance(maintenanceId, updateMaintenance);
        assertTrue(updateAnswer.isSuccessful());
        LOGGER.info("Update maintenance result = " + updateAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Maintenance updatedMaintenance = maintenanceService.getSingleMaintenanceById(maintenanceId);
        assertEquals(updateMaintenance, updatedMaintenance);

        IUDAnswer deleteAnswer = maintenanceService.deleteMaintenance(maintenanceId);
        assertTrue(deleteAnswer.isSuccessful());
        LOGGER.info("Delete maintenance result = " + deleteAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Maintenance deletedMaintenance = maintenanceService.getSingleMaintenanceById(maintenanceId);
        assertNull(deletedMaintenance);
    }

    @Test
    @Rollback(true)
    public void getAllMaintenances() {
        List<Maintenance> maintenances = maintenanceService.getAllMaintenances();
        for (Maintenance maintenance : maintenances) {
            LOGGER.info(maintenance);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleMaintenanceById(){
        Maintenance maintenance = maintenanceService.getSingleMaintenanceById(2007);
        LOGGER.info(maintenance);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteMaintenance(){
        int maintenanceId = 2054;
        IUDAnswer iudAnswer = maintenanceService.deleteMaintenance(maintenanceId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete maintenance result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createMaintenance(){
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenancePrice(534L);
        maintenance.setMaintenanceTitle("title main");
        maintenance.setMaintenanceType("type of main");
        IUDAnswer iudAnswer = maintenanceService.insertMaintenance(maintenance);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Insert maintenance result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateMaintenance(){
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenancePrice(600L);
        maintenance.setMaintenanceTitle("new title main");
        maintenance.setMaintenanceType("new type of main");
        IUDAnswer iudAnswer = maintenanceService.updateMaintenance(2054, maintenance);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update maintenance result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
