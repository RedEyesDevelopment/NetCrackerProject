package projectpackage.repository.ordersdao;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.User;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

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
            jdbcTemplate.update(insertObject, objectId, oldOrder.getObjectId(), 12, null, null);
            jdbcTemplate.update(insertAttribute, 43, objectId, null, new Date());
            jdbcTemplate.update(insertObjReference, 42, objectId, oldOrder.getLastModificator().getObjectId());

            if (oldOrder.getRegistrationDate().getTime() != newOrder.getRegistrationDate().getTime()) {
                jdbcTemplate.update(insertAttribute, 8, objectId, null, oldOrder.getRegistrationDate());
            }
            if (!oldOrder.getIsPaidFor().equals(newOrder.getIsPaidFor())) {
                if (oldOrder.getIsPaidFor()) {
                    jdbcTemplate.update(insertAttribute, 9, objectId, "true", null);
                } else {
                    jdbcTemplate.update(insertAttribute, 9, objectId, "false", null);
                }
            }
            if (!oldOrder.getIsConfirmed().equals(newOrder.getIsConfirmed())) {
                if (oldOrder.getIsConfirmed()) {
                    jdbcTemplate.update(insertAttribute, 10, objectId, "true", null);
                } else {
                    jdbcTemplate.update(insertAttribute, 10, objectId, "false", null);
                }
            }
            if (oldOrder.getLivingStartDate().getTime() != newOrder.getLivingStartDate().getTime()) {
                jdbcTemplate.update(insertAttribute, 11, objectId, null, oldOrder.getLivingStartDate());
            }
            if (oldOrder.getLivingFinishDate().getTime() != newOrder.getLivingFinishDate().getTime()) {
                jdbcTemplate.update(insertAttribute, 12, objectId, null, oldOrder.getLivingFinishDate());
            }
            if (!oldOrder.getSum().equals(newOrder.getSum())) {
                jdbcTemplate.update(insertAttribute, 13, objectId, oldOrder.getSum(), null);
            }
            if (!oldOrder.getComment().equals(newOrder.getComment())) {
                jdbcTemplate.update(insertAttribute, 14, objectId, oldOrder.getComment(), null);
            }
            if (oldOrder.getLastModificator().getObjectId() != newOrder.getLastModificator().getObjectId()) {
                jdbcTemplate.update(insertObjReference, 44, objectId, oldOrder.getLastModificator().getObjectId());
            }
            if (oldOrder.getRoom().getObjectId() != newOrder.getRoom().getObjectId()) {
                jdbcTemplate.update(insertObjReference, 6, objectId, oldOrder.getRoom().getObjectId());
            }
            if (oldOrder.getClient().getObjectId() != newOrder.getClient().getObjectId()) {
                jdbcTemplate.update(insertObjReference, 7, objectId, oldOrder.getClient().getObjectId());
            }
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
        return objectId;
    }

    @Override
    public void deleteModificationHistory(int id) throws ReferenceBreakException {
        deleteSingleEntityById(id);
    }
}
