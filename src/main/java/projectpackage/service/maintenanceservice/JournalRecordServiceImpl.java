package projectpackage.service.maintenanceservice;


import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
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
        List<JournalRecord> journalRecords = journalRecordDAO.getAllJournalRecords();
        if (null == journalRecords) LOGGER.info("Returned NULL!!!");
        return journalRecords;
    }

    @Override
    public List<JournalRecord> getAllJournalRecords(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public JournalRecord getSingleEntityById(int id) {
        JournalRecord journalRecord = journalRecordDAO.getJournalRecord(id);
        if (null == journalRecord) LOGGER.info("Returned NULL!!!");
        return journalRecord;
    }

    @Override
    public IUDAnswer deleteJournalRecord(int id) {
        try {
            journalRecordDAO.deleteJournalRecord(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id,false, e.printReferencesEntities());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn("Entity with that id does not exist!", e);
            return new IUDAnswer(id, "deletedObjectNotExists");
        } catch (WrongEntityIdException e) {
            LOGGER.warn("This id belong another entity class!", e);
            return new IUDAnswer(id, "wrongDeleteId");
        }
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertJournalRecord(JournalRecord journalRecord) {
        Integer journalRecordId = null;
        try {
            journalRecordId = journalRecordDAO.insertJournalRecord(journalRecord);
            LOGGER.info("Get from DB journalRecordId = " + journalRecordId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(journalRecordId,false, "transactionInterrupt");
        }
        return new IUDAnswer(journalRecordId,true);
    }

    @Override
    public IUDAnswer updateJournalRecord(int id, JournalRecord newJournalRecord) {
        try {
            newJournalRecord.setObjectId(id);
            JournalRecord oldJournalRecord = journalRecordDAO.getJournalRecord(id);
            journalRecordDAO.updateJournalRecord(newJournalRecord, oldJournalRecord);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, "transactionInterrupt");
        }
        return new IUDAnswer(id,true);
    }
}
