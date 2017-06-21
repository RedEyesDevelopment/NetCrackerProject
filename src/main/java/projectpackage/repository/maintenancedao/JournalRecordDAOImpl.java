package projectpackage.repository.maintenancedao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.*;
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
    public Integer insertJournalRecord(JournalRecord journalRecord) throws TransactionException {
        if (journalRecord == null) return null;
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(INSERT_OBJECT, objectId, journalRecord.getOrderId(), 16, null, null);

            jdbcTemplate.update(INSERT_ATTRIBUTE, 54, objectId, journalRecord.getCount(), null);
            jdbcTemplate.update(INSERT_ATTRIBUTE, 55, objectId, journalRecord.getCost(), null);
            jdbcTemplate.update(INSERT_ATTRIBUTE, 56, objectId, null, journalRecord.getUsedDate());

            if (journalRecord.getMaintenance() != null) {
                jdbcTemplate.update(INSERT_OBJ_REFERENCE, 53, objectId, journalRecord.getMaintenance().getObjectId());
            } else {
                throw new NullReferenceObjectException();
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public Integer updateJournalRecord(JournalRecord newJournalRecord, JournalRecord oldJournalRecord) throws TransactionException {
        if (oldJournalRecord == null || newJournalRecord == null) return null;
        try {
            updateCount(newJournalRecord, oldJournalRecord);
            updateCost(newJournalRecord, oldJournalRecord);
            updateUsedDate(newJournalRecord, oldJournalRecord);
            updateMaintenance(newJournalRecord, oldJournalRecord);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return newJournalRecord.getObjectId();
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

    private void updateCount(JournalRecord newJournalRecord, JournalRecord oldJournalRecord) {
        if (oldJournalRecord.getCount() != null && newJournalRecord.getCount() != null) {
            if (!oldJournalRecord.getCount().equals(newJournalRecord.getCount())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newJournalRecord.getCount(), null, newJournalRecord.getObjectId(), 54);
            }
        } else {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, newJournalRecord.getCount(), null, newJournalRecord.getObjectId(), 54);
        }
    }

    private void updateCost(JournalRecord newJournalRecord, JournalRecord oldJournalRecord) {
        if (oldJournalRecord.getCost() != null && newJournalRecord.getCost() != null) {
            if (!oldJournalRecord.getCost().equals(newJournalRecord.getCost())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newJournalRecord.getCost(), null, newJournalRecord.getObjectId(), 55);
            }
        } else {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, newJournalRecord.getCost(), null, newJournalRecord.getObjectId(), 55);
        }
    }

    private void updateUsedDate(JournalRecord newJournalRecord, JournalRecord oldJournalRecord) {
        if (oldJournalRecord.getUsedDate() != null && newJournalRecord.getUsedDate() != null) {
            if (oldJournalRecord.getUsedDate().getTime() != (newJournalRecord.getUsedDate().getTime())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newJournalRecord.getUsedDate(), newJournalRecord.getObjectId(), 56);
            }
        } else {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newJournalRecord.getUsedDate(), newJournalRecord.getObjectId(), 56);
        }
    }

    private void updateMaintenance(JournalRecord newJournalRecord, JournalRecord oldJournalRecord) {
        if (oldJournalRecord.getMaintenance() != null && newJournalRecord.getMaintenance() != null) {
            if (oldJournalRecord.getMaintenance().getObjectId() != newJournalRecord.getMaintenance().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newJournalRecord.getMaintenance().getObjectId(),
                        newJournalRecord.getObjectId(), 53);
            }
        } else {
            throw new NullReferenceObjectException();
        }
    }
}
