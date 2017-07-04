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
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.Date;
import java.util.List;

@Log4j
@Repository
public class ModificationHistoryDAOImpl extends AbstractDAO implements ModificationHistoryDAO {
    private static final Logger LOGGER = Logger.getLogger(ModificationHistoryDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public ModificationHistory getModificationHistory(Integer id) {
        if (null == id) {
            return null;
        }

        return (ModificationHistory) manager.createReactEAV(ModificationHistory.class)
                .fetchRootReference(User.class,"UserToModificationHistory")
                .closeAllFetches().getSingleEntityWithId(id);
    }

    @Override
    public List<ModificationHistory> getAllModificationHistories() {
        return (List<ModificationHistory>) manager.createReactEAV(ModificationHistory.class)
                .fetchRootReference(User.class,"UserToModificationHistory")
                .closeAllFetches().getEntityCollection();
    }

    @Override
    public Integer insertModificationHistory(Order newOrder, Order oldOrder) {
        if (newOrder == null || oldOrder == null) {
            return null;
        }
        Integer objectId = nextObjectId();

        jdbcTemplate.update(INSERT_OBJECT, objectId, oldOrder.getObjectId(), 12, null, null);
        jdbcTemplate.update(INSERT_ATTRIBUTE, 43, objectId, null, new Date());
        insertRegistrationDate(newOrder, oldOrder, objectId);
        insertIsPaidFor(newOrder, oldOrder, objectId);
        insertIsConfirmed(newOrder, oldOrder, objectId);
        insertLivingStartDate(newOrder, oldOrder, objectId);
        insertLivingFinishDate(newOrder, oldOrder, objectId);
        insertSum(newOrder, oldOrder, objectId);
        insertComment(newOrder, oldOrder, objectId);
        insertCategory(newOrder, oldOrder, objectId);
        insertRoom(newOrder, oldOrder, objectId);
        insertClient(newOrder, oldOrder, objectId);
        insertLastMidificator(newOrder, oldOrder, objectId);

        return objectId;
    }

    @Override
    public void deleteModificationHistory(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        ModificationHistory modificationHistory = null;
        try {
            modificationHistory = getModificationHistory(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == modificationHistory) {
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }

    private void insertLastMidificator(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getLastModificator() != null && newOrder.getLastModificator() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 42, objectId, oldOrder.getLastModificator().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertClient(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getClient() != null && newOrder.getClient() != null) {
            if (oldOrder.getClient().getObjectId() != newOrder.getClient().getObjectId()) {
                jdbcTemplate.update(INSERT_OBJ_REFERENCE, 60, objectId, oldOrder.getClient().getObjectId());
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertRoom(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getRoom() != null && newOrder.getRoom() != null) {
            if (oldOrder.getRoom().getObjectId() != newOrder.getRoom().getObjectId()) {
                jdbcTemplate.update(INSERT_OBJ_REFERENCE, 59, objectId, oldOrder.getRoom().getObjectId());
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertCategory(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getCategory() != null && newOrder.getCategory() != null) {
            if (oldOrder.getCategory().getObjectId() != newOrder.getCategory().getObjectId()) {
                jdbcTemplate.update(INSERT_OBJ_REFERENCE, 68, objectId, oldOrder.getCategory().getObjectId());
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertComment(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getComment() != null && newOrder.getComment() != null && !newOrder.getComment().isEmpty()) {
            if (!oldOrder.getComment().equals(newOrder.getComment())) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 67, objectId, oldOrder.getComment(), null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 67, objectId, null, null);
            }
        } else if (newOrder.getComment() != null && !newOrder.getComment().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 67, objectId, oldOrder.getComment(), null);
        } else if (oldOrder.getComment() != null){
            jdbcTemplate.update(INSERT_ATTRIBUTE, 67, objectId, null, null);
        }
    }

    private void insertSum(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getSum() != null && newOrder.getSum() != null) {
            if (!oldOrder.getSum().equals(newOrder.getSum())) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 66, objectId, oldOrder.getSum(), null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 66, objectId, null, null);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertLivingFinishDate(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getLivingFinishDate() != null && newOrder.getLivingFinishDate() != null) {
            if (oldOrder.getLivingFinishDate().getTime() != newOrder.getLivingFinishDate().getTime()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 65, objectId, null, oldOrder.getLivingFinishDate());
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 65, objectId, null, null);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertLivingStartDate(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getLivingStartDate() != null && newOrder.getLivingStartDate() != null) {
            if (oldOrder.getLivingStartDate().getTime() != newOrder.getLivingStartDate().getTime()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 64, objectId, null, oldOrder.getLivingStartDate());
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 64, objectId, null, null);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertIsConfirmed(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getIsConfirmed() != null && newOrder.getIsConfirmed() != null) {
            if (oldOrder.getIsConfirmed()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 63, objectId, "true", null);
            } else if (!oldOrder.getIsConfirmed()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 63, objectId, "false", null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 63, objectId, null, null);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertIsPaidFor(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getIsPaidFor() != null && newOrder.getIsPaidFor() != null) {
            if (oldOrder.getIsPaidFor()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 62, objectId, "true", null);
            } else if (!oldOrder.getIsPaidFor()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 62, objectId, "false", null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 63, objectId, null, null);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertRegistrationDate(Order newOrder, Order oldOrder, Integer objectId) {
        if (oldOrder.getRegistrationDate() != null && newOrder.getRegistrationDate() != null) {
            if (oldOrder.getRegistrationDate().getTime() != newOrder.getRegistrationDate().getTime()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 61, objectId, null, oldOrder.getRegistrationDate());
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 61, objectId, null, null);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
