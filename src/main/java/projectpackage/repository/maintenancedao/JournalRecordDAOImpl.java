package projectpackage.repository.maintenancedao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Dima on 21.05.2017.
 */
@Repository
public class JournalRecordDAOImpl extends AbstractDAO implements JournalRecordDAO {
    private static final Logger LOGGER = Logger.getLogger(JournalRecordDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public JournalRecord getJournalRecord(Integer id) {
        if (null == id) return null;
        try {
            return (JournalRecord) manager.createReactEAV(JournalRecord.class)
                    .fetchRootReference(Maintenance.class, "MaintenanceToJournalRecord")
                    .closeAllFetches()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<JournalRecord> getAllJournalRecords() {
        try {
            return manager.createReactEAV(JournalRecord.class)
                    .fetchRootReference(Maintenance.class, "MaintenanceToJournalRecord")
                    .closeAllFetches()
                    .getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertJournalRecord(JournalRecord journalRecord) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, journalRecord.getOrderId(), 16, null, null);

            jdbcTemplate.update(insertAttribute, 54, objectId, journalRecord.getCount(), null);
            jdbcTemplate.update(insertAttribute, 55, objectId, journalRecord.getCost(), null);
            jdbcTemplate.update(insertAttribute, 56, objectId, null, journalRecord.getUsedDate());

            jdbcTemplate.update(insertObjReference, 53, objectId, journalRecord.getMaintenance().getObjectId());

        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateJournalRecord(JournalRecord newJournalRecord, JournalRecord oldJournalRecord) throws TransactionException {
        try {
            if (!oldJournalRecord.getCount().equals(newJournalRecord.getCount())) {
                jdbcTemplate.update(updateAttribute, newJournalRecord.getCount(), null, newJournalRecord.getObjectId(), 54);
            }
            if (!oldJournalRecord.getCost().equals(newJournalRecord.getCost())) {
                jdbcTemplate.update(updateAttribute, newJournalRecord.getCost(), null, newJournalRecord.getObjectId(), 55);
            }
            if (oldJournalRecord.getUsedDate().getTime() !=(newJournalRecord.getUsedDate().getTime())) {
                jdbcTemplate.update(updateAttribute, null, newJournalRecord.getUsedDate(), newJournalRecord.getObjectId(), 56);
            }
            if (oldJournalRecord.getMaintenance().getObjectId() != newJournalRecord.getMaintenance().getObjectId()) {
                jdbcTemplate.update(updateReference, newJournalRecord.getMaintenance().getObjectId(), newJournalRecord.getObjectId(), 53);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deleteJournalRecord(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        JournalRecord journalRecord = null;
        try {
            journalRecord = getJournalRecord(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == journalRecord) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
