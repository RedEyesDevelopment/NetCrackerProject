package projectpackage.service.maintenanceservice;


import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.maintenancedao.JournalRecordDAO;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
@Log4j
@Service
public class JournalRecordServiceImpl implements JournalRecordService{
    private static final Logger LOGGER = Logger.getLogger(JournalRecordServiceImpl.class);

    @Autowired
    JournalRecordDAO journalRecordDAO;

    @Override
    public List<JournalRecord> getAllJournalRecords() {
        return null;
    }

    @Override
    public List<JournalRecord> getAllJournalRecords(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public JournalRecord getSingleEntityById(int id) {
        return null;
    }

    @Override
    public boolean deleteJournalRecord(int id) {
        int count = journalRecordDAO.deleteJournalRecord(id);
        LOGGER.info("Deleted rows : " + count);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertJournalRecord(JournalRecord journalRecord) {
        try {
            int journalRecordId = journalRecordDAO.insertJournalRecord(journalRecord);
            LOGGER.info("Get from DB journalRecordId = " + journalRecordId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateJournalRecord(int id, JournalRecord newJournalRecord) {
        try {
            newJournalRecord.setObjectId(id);
            JournalRecord oldJournalRecord = journalRecordDAO.getJournalRecord(id);
            journalRecordDAO.updateJournalRecord(newJournalRecord, oldJournalRecord);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
