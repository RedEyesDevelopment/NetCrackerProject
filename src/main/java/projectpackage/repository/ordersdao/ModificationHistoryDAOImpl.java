package projectpackage.repository.ordersdao;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.User;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 21.05.2017.
 */
@Log4j
@Repository
public class ModificationHistoryDAOImpl extends AbstractDAO implements ModificationHistoryDAO {
    private static final Logger LOGGER = Logger.getLogger(ModificationHistoryDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public ModificationHistory getModificationHistory(Integer id) {
        if (null == id) return null;
        try {
            return (ModificationHistory) manager.createReactEAV(ModificationHistory.class)
                    .fetchRootReference(User.class,"UserToModificationHistory")
                    .closeAllFetches().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<ModificationHistory> getAllModificationHistories() {
        try {
            return (List<ModificationHistory>) manager.createReactEAV(ModificationHistory.class)
                    .fetchRootReference(User.class,"UserToModificationHistory")
                    .closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertModificationHistory(Order newOrder, Order oldOrder) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(INSERT_OBJECT, objectId, oldOrder.getObjectId(), 12, null, null);

            if (oldOrder.getRegistrationDate().getTime() != newOrder.getRegistrationDate().getTime()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 61, objectId, null, oldOrder.getRegistrationDate());
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 61, objectId, null, null);
            }

            if (oldOrder.getIsPaidFor()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 62, objectId, "true", null);
            } else if (!oldOrder.getIsPaidFor()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 62, objectId, "false", null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 63, objectId, null, null);
            }

            if (oldOrder.getIsConfirmed()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 63, objectId, "true", null);
            } else if (!oldOrder.getIsConfirmed()){
                jdbcTemplate.update(INSERT_ATTRIBUTE, 63, objectId, "false", null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 63, objectId, null, null);
            }

            if (oldOrder.getLivingStartDate().getTime() != newOrder.getLivingStartDate().getTime()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 64, objectId, null, oldOrder.getLivingStartDate());
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 64, objectId, null, null);
            }

            if (oldOrder.getLivingFinishDate().getTime() != newOrder.getLivingFinishDate().getTime()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 65, objectId, null, oldOrder.getLivingFinishDate());
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 65, objectId, null, null);
            }

            if (!oldOrder.getSum().equals(newOrder.getSum())) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 66, objectId, oldOrder.getSum(), null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 66, objectId, null, null);
            }

            if (!oldOrder.getComment().equals(newOrder.getComment())) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 67, objectId, oldOrder.getComment(), null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 67, objectId, null, null);
            }

            if (oldOrder.getCategory().getObjectId() != newOrder.getCategory().getObjectId()) {
                jdbcTemplate.update(INSERT_OBJ_REFERENCE, 68, objectId, oldOrder.getCategory().getObjectId());
            }

            if (oldOrder.getRoom().getObjectId() != newOrder.getRoom().getObjectId()) {
                jdbcTemplate.update(INSERT_OBJ_REFERENCE, 59, objectId, oldOrder.getRoom().getObjectId());
            }

            if (oldOrder.getClient().getObjectId() != newOrder.getClient().getObjectId()) {
                jdbcTemplate.update(INSERT_OBJ_REFERENCE, 60, objectId, oldOrder.getClient().getObjectId());
            }

            jdbcTemplate.update(INSERT_ATTRIBUTE, 43, objectId, null, new Date());
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 42, objectId, oldOrder.getLastModificator().getObjectId());
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void deleteModificationHistory(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        ModificationHistory modificationHistory = null;
        try {
            modificationHistory = getModificationHistory(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == modificationHistory) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
