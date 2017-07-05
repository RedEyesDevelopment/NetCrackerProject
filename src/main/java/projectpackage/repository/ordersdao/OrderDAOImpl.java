package projectpackage.repository.ordersdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;

@Repository
public class OrderDAOImpl extends AbstractDAO implements OrderDAO{
    private static final Logger LOGGER = Logger.getLogger(OrderDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Order> getAllOrderForAdmin() {
        return  manager.createReactEAV(Order.class).fetchRootReference(User.class, "UserToOrderAsClient")
                .fetchInnerChild(Phone.class).closeAllFetches()
                .fetchRootReference(User.class, "UserToOrderAsLastModificator")
                .closeAllFetches()
                .fetchRootReference(Room.class, "RoomToOrder")
                .fetchInnerReference(RoomType.class, "RoomTypeToRoom")
                .closeAllFetches()
                .fetchRootChild(JournalRecord.class)
                .fetchInnerReference(Maintenance.class, "MaintenanceToJournalRecord")
                .closeAllFetches()
                .fetchRootReference(Category.class, "CategoryToOrder")
                .closeAllFetches()
                .getEntityCollection();
    }

    @Override
    public List<Order> getSimpleOrderList() {
        return manager.createReactEAV(Order.class).getEntityCollection();
    }

    @Override
    public Order getOrderForAdmin(Integer id) {
        if (id == null) {
            return null;
        }
        return (Order) manager.createReactEAV(Order.class).fetchRootReference(User.class, "UserToOrderAsClient")
                .fetchInnerChild(Phone.class).closeAllFetches()
                .fetchRootReference(User.class, "UserToOrderAsLastModificator")
                .closeAllFetches()
                .fetchRootReference(Room.class, "RoomToOrder")
                .fetchInnerReference(RoomType.class, "RoomTypeToRoom")
                .closeAllFetches()
                .fetchRootChild(JournalRecord.class)
                .fetchInnerReference(Maintenance.class, "MaintenanceToJournalRecord")
                .closeAllFetches()
                .fetchRootReference(Category.class, "CategoryToOrder")
                .closeAllFetches()
                .getSingleEntityWithId(id);
    }

    @Override
    public Order getOrder(Integer id) {
        if (null == id) {
            return null;
        }

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
                .fetchRootReference(Category.class, "CategoryToOrder")
                .fetchInnerChild(Complimentary.class)
                .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary")
                .closeAllFetches()
                .getSingleEntityWithId(id);
    }

    @Override
    public List<Order> getAllOrder() {
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
                .fetchRootReference(Category.class, "CategoryToOrder")
                .fetchInnerChild(Complimentary.class)
                .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary")
                .closeAllFetches()
                .getEntityCollection();
    }

    @Override
    public Integer insertOrder(Order order) {
        if (order == null) {
            return null;
        }
        Integer objectId = nextObjectId();

        jdbcTemplate.update(INSERT_OBJECT, objectId, null, 2, null, null);
        jdbcTemplate.update(INSERT_ATTRIBUTE, 5, objectId, objectId, null);

        insertRegistrationDate(order, objectId);
        insertIsPaidFor(order, objectId);
        insertIsConfirmed(order, objectId);
        insertLivingStartDate(order, objectId);
        insertLivingFinishDate(order, objectId);
        insertSum(order, objectId);
        insertComment(order, objectId);
        insertLastModificator(order, objectId);
        insertRoom(order, objectId);
        insertClient(order, objectId);
        insertCategory(order, objectId);

        return objectId;
    }

    @Override
    public Integer updateOrder(Order newOrder, Order oldOrder) {
        if (newOrder == null || oldOrder == null) {
            return null;
        }

        updateRegistrationDate(newOrder, oldOrder);
        updateIsPaidFor(newOrder, oldOrder);
        updateIsConfirmed(newOrder, oldOrder);
        updateLivingStartDate(newOrder, oldOrder);
        updateLivingFinishDate(newOrder, oldOrder);
        updateSum(newOrder, oldOrder);
        updateComment(newOrder, oldOrder);
        updateLastModificator(newOrder, oldOrder);
        updateRoom(newOrder, oldOrder);
        updateClient(newOrder, oldOrder);
        updateCategory(newOrder, oldOrder);

        return newOrder.getObjectId();
    }

    @Override
    public void deleteOrder(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        Order order = null;
        try {
            order = getOrder(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == order) {
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }

    private void insertCategory(Order order, Integer objectId) {
        if (order.getCategory() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 57, objectId, order.getCategory().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertClient(Order order, Integer objectId) {
        if (order.getClient() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 7, objectId, order.getClient().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertRoom(Order order, Integer objectId) {
        if (order.getRoom() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 6, objectId, order.getRoom().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertLastModificator(Order order, Integer objectId) {
        if (order.getLastModificator() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 44, objectId, order.getLastModificator().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertComment(Order order, Integer objectId) {
        if (order.getComment() != null && !order.getComment().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 14, objectId, order.getComment(), null);
        } else {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 14, objectId, null, null);
        }
    }

    private void insertSum(Order order, Integer objectId) {
        if (order.getSum() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 13, objectId, order.getSum(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertLivingFinishDate(Order order, Integer objectId) {
        if (order.getLivingFinishDate() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 12, objectId, null, order.getLivingFinishDate());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertLivingStartDate(Order order, Integer objectId) {
        if (order.getLivingStartDate() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 11, objectId, null, order.getLivingStartDate());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertIsConfirmed(Order order, Integer objectId) {
        if (order.getIsConfirmed() != null) {
            if (order.getIsConfirmed()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 10, objectId, "true", null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 10, objectId, "false", null);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertIsPaidFor(Order order, Integer objectId) {
        if (order.getIsPaidFor() != null) {
            if (order.getIsPaidFor()) {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 9, objectId, "true", null);
            } else {
                jdbcTemplate.update(INSERT_ATTRIBUTE, 9, objectId, "false", null);      //Is_paid_for
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertRegistrationDate(Order order, Integer objectId) {
        if (order.getRegistrationDate() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 8, objectId, null, order.getRegistrationDate());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateCategory(Order newOrder, Order oldOrder) {
        if (oldOrder.getCategory() != null && newOrder.getCategory() != null) {
            if (oldOrder.getCategory().getObjectId() != newOrder.getCategory().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newOrder.getCategory().getObjectId(), newOrder.getObjectId(), 57);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateClient(Order newOrder, Order oldOrder) {
        if (oldOrder.getClient() != null && newOrder.getClient() != null) {
            if (oldOrder.getClient().getObjectId() != newOrder.getClient().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newOrder.getClient().getObjectId(), newOrder.getObjectId(), 7);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateRoom(Order newOrder, Order oldOrder) {
        if (oldOrder.getRoom() != null && newOrder.getRoom() != null) {
            if (oldOrder.getRoom().getObjectId() != newOrder.getRoom().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newOrder.getRoom().getObjectId(), newOrder.getObjectId(), 6);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateLastModificator(Order newOrder, Order oldOrder) {
        if (oldOrder.getLastModificator() != null && newOrder.getLastModificator() != null) {
            if (oldOrder.getLastModificator().getObjectId() != newOrder.getLastModificator().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newOrder.getLastModificator().getObjectId(), newOrder.getObjectId(), 44);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateComment(Order newOrder, Order oldOrder) {
        if (oldOrder.getComment() != null && newOrder.getComment() != null && !newOrder.getComment().isEmpty()) {
            if (!oldOrder.getComment().equals(newOrder.getComment())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newOrder.getComment(), null, newOrder.getObjectId(), 14);
            }
        } else if (newOrder.getComment() != null && !newOrder.getComment().isEmpty()) {
            jdbcTemplate.update(UPDATE_ATTRIBUTE, newOrder.getComment(), null, newOrder.getObjectId(), 14);
        } else if (oldOrder.getComment() != null){
            jdbcTemplate.update(UPDATE_ATTRIBUTE, null, null, newOrder.getObjectId(), 14);
        }
    }

    private void updateSum(Order newOrder, Order oldOrder) {
        if (oldOrder.getSum() != null && newOrder.getSum() != null) {
            if (!oldOrder.getSum().equals(newOrder.getSum())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newOrder.getSum(), null, newOrder.getObjectId(), 13);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateLivingFinishDate(Order newOrder, Order oldOrder) {
        if (oldOrder.getLivingFinishDate() != null && newOrder.getLivingFinishDate() != null) {
            if (oldOrder.getLivingFinishDate().getTime() != newOrder.getLivingFinishDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newOrder.getLivingFinishDate(), newOrder.getObjectId(), 12);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateLivingStartDate(Order newOrder, Order oldOrder) {
        if (oldOrder.getLivingStartDate() != null && newOrder.getLivingStartDate() != null) {
            if (oldOrder.getLivingStartDate().getTime() != newOrder.getLivingStartDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newOrder.getLivingStartDate(), newOrder.getObjectId(), 11);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateIsConfirmed(Order newOrder, Order oldOrder) {
        if (oldOrder.getIsConfirmed() != null && newOrder.getIsConfirmed() != null) {
            if (!oldOrder.getIsConfirmed().equals(newOrder.getIsConfirmed())) {
                if (newOrder.getIsConfirmed()) {
                    jdbcTemplate.update(UPDATE_ATTRIBUTE, "true", null, newOrder.getObjectId(), 10);
                } else {
                    jdbcTemplate.update(UPDATE_ATTRIBUTE, "false", null, newOrder.getObjectId(), 10);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateIsPaidFor(Order newOrder, Order oldOrder) {
        if (oldOrder.getIsPaidFor() != null && newOrder.getIsPaidFor() != null) {
            if (!oldOrder.getIsPaidFor().equals(newOrder.getIsPaidFor())) {
                if (newOrder.getIsPaidFor()) {
                    jdbcTemplate.update(UPDATE_ATTRIBUTE, "true", null, newOrder.getObjectId(), 9);
                } else {
                    jdbcTemplate.update(UPDATE_ATTRIBUTE, "false", null, newOrder.getObjectId(), 9);
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateRegistrationDate(Order newOrder, Order oldOrder) {
        if (oldOrder.getRegistrationDate() != null && newOrder.getRegistrationDate() != null) {
            if (oldOrder.getRegistrationDate().getTime() != newOrder.getRegistrationDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newOrder.getRegistrationDate(), newOrder.getObjectId(), 8);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
