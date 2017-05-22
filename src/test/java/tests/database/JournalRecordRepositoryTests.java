package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.orders.Category;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.maintenanceservice.JournalRecordService;

import java.util.Date;
import java.util.List;

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
        JournalRecord journalRecord = journalRecordService.getSingleEntityById(2007);// check id
        LOGGER.info(journalRecord);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteJournalRecord(){
        int journalRecordId = 2008;
        IUDAnswer iudAnswer = journalRecordService.deleteJournalRecord(journalRecordId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete journalRecord result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createJournalRecord(){
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(435);
        JournalRecord journalRecord = new JournalRecord();
        journalRecord.setCost(3234L);
        journalRecord.setCount(34);
        journalRecord.setUsedDate(new Date());
        journalRecord.setOrderId(432);
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
        maintenance.setObjectId(433);
        JournalRecord journalRecord = new JournalRecord();
        journalRecord.setCost(4000L);
        journalRecord.setCount(50);
        journalRecord.setUsedDate(new Date());
        journalRecord.setOrderId(431);
        journalRecord.setMaintenance(maintenance);
        IUDAnswer iudAnswer = journalRecordService.updateJournalRecord(432, journalRecord);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update journalRecord result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
