package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.maintenanceservice.JournalRecordService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 22.05.2017.
 */
@Log4j
public class JournalRecordRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(JournalRecordRepositoryTests.class);

    @Autowired
    JournalRecordService journalRecordService;

    @Test
    @Rollback
    public void crudJournalRecordTest() {
        Maintenance insertMaintenance = new Maintenance();
        insertMaintenance.setObjectId(1500);
        insertMaintenance.setMaintenanceType("odezhda");
        insertMaintenance.setMaintenanceTitle("washing");
        insertMaintenance.setMaintenancePrice(300L);
        JournalRecord insertJournalRecord = new JournalRecord();
        insertJournalRecord.setCost(3234L);
        insertJournalRecord.setCount(34);
        insertJournalRecord.setUsedDate(new Date(16000L));
        insertJournalRecord.setOrderId(300);
        insertJournalRecord.setMaintenance(insertMaintenance);
        IUDAnswer insertAnswer = journalRecordService.insertJournalRecord(insertJournalRecord);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Insert journalRecord result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int journalRecordId = insertAnswer.getObjectId();
        JournalRecord insertedJournalRecord = journalRecordService.getSingleEntityById(journalRecordId);
        insertJournalRecord.setObjectId(journalRecordId);
        assertEquals(insertJournalRecord, insertedJournalRecord);

        Maintenance updateMaintenance = new Maintenance();
        updateMaintenance.setObjectId(1501);
        updateMaintenance.setMaintenancePrice(400L);
        updateMaintenance.setMaintenanceTitle("breakfast");
        updateMaintenance.setMaintenanceType("food");
        JournalRecord updateJournalRecord = new JournalRecord();
        updateJournalRecord.setCost(4000L);
        updateJournalRecord.setCount(50);
        updateJournalRecord.setUsedDate(new Date(17000L));
        updateJournalRecord.setOrderId(300);
        updateJournalRecord.setMaintenance(updateMaintenance);
        IUDAnswer iudAnswer = journalRecordService.updateJournalRecord(journalRecordId, updateJournalRecord);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update journalRecord result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        JournalRecord updatedJournalRecord = journalRecordService.getSingleEntityById(journalRecordId);
        assertEquals(updateJournalRecord, updatedJournalRecord);

        IUDAnswer deleteAnswer = journalRecordService.deleteJournalRecord(journalRecordId);
        assertTrue(deleteAnswer.isSuccessful());
        LOGGER.info("Delete journalRecord result = " + deleteAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        JournalRecord deletedJournalRecord = journalRecordService.getSingleEntityById(journalRecordId);
        assertNull(deletedJournalRecord);
    }

    @Test
    @Rollback(true)
    public void getAllJournalRecords() {
        List<JournalRecord> journalRecords = journalRecordService.getAllJournalRecords();
        for (JournalRecord journalRecord : journalRecords) {
            LOGGER.info(journalRecord);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleJournalRecordById(){
        JournalRecord journalRecord = journalRecordService.getSingleEntityById(2007);
        LOGGER.info(journalRecord);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteJournalRecord(){
        int journalRecordId = 2053;
        IUDAnswer iudAnswer = journalRecordService.deleteJournalRecord(journalRecordId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete journalRecord result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createJournalRecord(){
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(1500);
        JournalRecord journalRecord = new JournalRecord();
        journalRecord.setCost(3234L);
        journalRecord.setCount(34);
        journalRecord.setUsedDate(new Date());
        journalRecord.setOrderId(300);
        journalRecord.setMaintenance(maintenance);
        IUDAnswer iudAnswer = journalRecordService.insertJournalRecord(journalRecord);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Insert journalRecord result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateJournalRecord(){
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(1501);
        JournalRecord journalRecord = new JournalRecord();
        journalRecord.setCost(4000L);
        journalRecord.setCount(50);
        journalRecord.setUsedDate(new Date());
        journalRecord.setOrderId(301);
        journalRecord.setMaintenance(maintenance);
        IUDAnswer iudAnswer = journalRecordService.updateJournalRecord(2053, journalRecord);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update journalRecord result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
