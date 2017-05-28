package projectpackage.repository.ordersdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rates.Price;
import projectpackage.model.rates.Rate;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;
import projectpackage.repository.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Repository
public class OrderDAOImpl extends AbstractDAO implements OrderDAO{
    private static final Logger LOGGER = Logger.getLogger(OrderDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public Order getOrder(Integer id) {
        if (null == id) return null;
        try {
            return (Order) manager.createReactEAV(Order.class)
                    .fetchRootReference(User.class, "UserToOrderAsClient")
                    .fetchInnerChild(Phone.class).closeFetch()
                    .fetchInnerReference(Role.class, "RoleToUser")
                    .closeAllFetches()
                    .fetchRootReference(User.class, "UserToOrderAsLastModificator")
                    .fetchInnerReference(Role.class, "RoleToUser").closeFetch()
                    .fetchInnerChild(Phone.class)
                    .closeAllFetches()
                    .fetchRootReference(Room.class, "RoomToOrder").addCondition(new PriceEqualsToRoomCondition(), ConditionExecutionMoment.AFTER_QUERY)
                    .fetchInnerReference(RoomType.class, "RoomTypeToRoom")
                    .fetchInnerChild(Rate.class)
                    .fetchInnerChild(Price.class)
                    .closeAllFetches()
                    .fetchRootChild(JournalRecord.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToJournalRecord")
                    .closeAllFetches()
                    .fetchRootReference(Category.class, "OrderToCategory")
                    .fetchInnerChild(Complimentary.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary")
                    .closeAllFetches()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Order> getAllOrder() {
        try {
            return (List<Order>) manager.createReactEAV(Order.class)
                    .fetchRootReference(User.class, "UserToOrderAsClient")
                    .fetchInnerChild(Phone.class).closeFetch()
                    .fetchInnerReference(Role.class, "RoleToUser")
                    .closeAllFetches()
                    .fetchRootReference(User.class, "UserToOrderAsLastModificator")
                    .fetchInnerReference(Role.class, "RoleToUser").closeFetch()
                    .fetchInnerChild(Phone.class)
                    .closeAllFetches()
                    .fetchRootReference(Room.class, "RoomToOrder").addCondition(new PriceEqualsToRoomCondition(), ConditionExecutionMoment.AFTER_QUERY)
                    .fetchInnerReference(RoomType.class, "RoomTypeToRoom")
                    .fetchInnerChild(Rate.class)
                    .fetchInnerChild(Price.class)
                    .closeAllFetches()
                    .fetchRootChild(JournalRecord.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToJournalRecord")
                    .closeAllFetches()
                    .fetchRootReference(Category.class, "OrderToCategory")
                    .fetchInnerChild(Complimentary.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary")
                    .closeAllFetches()
                    .getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertOrder(Order order) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 2, null, null);                      //2 = Order

            jdbcTemplate.update(insertAttribute, 5, objectId, objectId, null);
            jdbcTemplate.update(insertAttribute, 8, objectId, null, order.getRegistrationDate());     //Registration_date
            if (order.getIsPaidFor()) {
                jdbcTemplate.update(insertAttribute, 9, objectId, "true", null);      //Is_paid_for
            } else {
                jdbcTemplate.update(insertAttribute, 9, objectId, "false", null);      //Is_paid_for
            }
            if (order.getIsConfirmed()) {
                jdbcTemplate.update(insertAttribute, 10, objectId, "true", null);
            } else {
                jdbcTemplate.update(insertAttribute, 10, objectId, "false", null);
            }
            jdbcTemplate.update(insertAttribute, 11, objectId, null, order.getLivingStartDate());     //Living_start_date
            jdbcTemplate.update(insertAttribute, 12, objectId, null, order.getLivingFinishDate());    //Living_finish_date
            jdbcTemplate.update(insertAttribute, 13, objectId, order.getSum(), null);               //Sum
            jdbcTemplate.update(insertAttribute, 14, objectId, order.getComment(), null);           //Comment

            jdbcTemplate.update(insertObjReference, 44, objectId, order.getLastModificator().getObjectId());
            jdbcTemplate.update(insertObjReference, 6, objectId, order.getRoom().getObjectId());     //Booked
            jdbcTemplate.update(insertObjReference, 7, objectId, order.getClient().getObjectId());     //Belong
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateOrder(Order newOrder, Order oldOrder) throws TransactionException {
        try {
            if (oldOrder.getRegistrationDate().getTime() != newOrder.getRegistrationDate().getTime()) {
                jdbcTemplate.update(updateAttribute, null,  newOrder.getRegistrationDate(), newOrder.getObjectId(), 8);
            }
            if (!oldOrder.getIsPaidFor().equals(newOrder.getIsPaidFor())) {
                if (newOrder.getIsPaidFor()) {
                    jdbcTemplate.update(updateAttribute, "true", null, newOrder.getObjectId(), 9);
                } else {
                    jdbcTemplate.update(updateAttribute, "false", null, newOrder.getObjectId(), 9);
                }
            }
            if (!oldOrder.getIsConfirmed().equals(newOrder.getIsConfirmed())) {
                if (newOrder.getIsConfirmed()) {
                    jdbcTemplate.update(updateAttribute, "true", null, newOrder.getObjectId(), 10);
                } else {
                    jdbcTemplate.update(updateAttribute, "false", null, newOrder.getObjectId(), 10);
                }
            }
            if (oldOrder.getLivingStartDate().getTime() != newOrder.getLivingStartDate().getTime()) {
                jdbcTemplate.update(updateAttribute, null, newOrder.getLivingStartDate(), newOrder.getObjectId(), 11);
            }
            if (oldOrder.getLivingFinishDate().getTime() != newOrder.getLivingFinishDate().getTime()) {
                jdbcTemplate.update(updateAttribute, null,  newOrder.getLivingFinishDate(), newOrder.getObjectId(), 12);
            }
            if (!oldOrder.getSum().equals(newOrder.getSum())) {
                jdbcTemplate.update(updateAttribute, newOrder.getSum(), null, newOrder.getObjectId(), 13);
            }
            if (!oldOrder.getComment().equals(newOrder.getComment())) {
                jdbcTemplate.update(updateAttribute, newOrder.getComment(), null, newOrder.getObjectId(), 14);
            }
            if (oldOrder.getLastModificator().getObjectId() != newOrder.getLastModificator().getObjectId()) {
                jdbcTemplate.update(updateReference, newOrder.getLastModificator().getObjectId(), newOrder.getObjectId(), 44);
            }
            if (oldOrder.getRoom().getObjectId() != newOrder.getRoom().getObjectId()) {
                jdbcTemplate.update(updateReference, newOrder.getRoom().getObjectId(), newOrder.getObjectId(), 6);
            }
            if (oldOrder.getClient().getObjectId() != newOrder.getClient().getObjectId()) {
                jdbcTemplate.update(updateReference, newOrder.getClient().getObjectId(), newOrder.getObjectId(), 7);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deleteOrder(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Order order = null;
        try {
            order = getOrder(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == order) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
