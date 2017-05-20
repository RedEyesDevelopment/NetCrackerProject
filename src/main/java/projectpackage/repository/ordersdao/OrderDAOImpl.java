package projectpackage.repository.ordersdao;

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
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.conditions.PriceEqualsToRoomCondition;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Repository
public class OrderDAOImpl extends AbstractDAO implements OrderDAO{

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public Order getOrder(Integer id) {
        if (null == id) return null;
        try {
            return (Order) manager.createReactEAV(Order.class).fetchReferenceEntityCollection(User.class, "UserToOrderAsClient").fetchChildEntityCollectionForInnerObject(Phone.class).closeAllFetches().fetchReferenceEntityCollection(User.class, "UserToOrderAsLastModificator").fetchReferenceEntityCollectionForInnerObject(Role.class, "RoleToUser").closeAllFetches().fetchReferenceEntityCollection(Room.class, "RoomToOrder").fetchReferenceEntityCollectionForInnerObject(RoomType.class, "RoomTypeToRoom").fetchChildEntityCollectionForInnerObject(Rate.class).fetchChildEntityCollectionForInnerObject(Price.class).closeAllFetches().fetchChildEntityCollection(JournalRecord.class).closeAllFetches().fetchReferenceEntityCollection(Category.class, "OrderToCategory").fetchChildEntityCollectionForInnerObject(Complimentary.class).fetchReferenceEntityCollectionForInnerObject(Maintenance.class, "MaintenanceToComplimentary").closeAllFetches().addCondition(PriceEqualsToRoomCondition.class).getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            return null;
        }
    }

    @Override
    public List<Order> getAllOrder() {
        try {
            return (List<Order>) manager.createReactEAV(Order.class).fetchReferenceEntityCollection(User.class, "UserToOrderAsClient").fetchChildEntityCollectionForInnerObject(Phone.class).closeAllFetches().fetchReferenceEntityCollection(User.class, "UserToOrderAsLastModificator").fetchReferenceEntityCollectionForInnerObject(Role.class, "RoleToUser").closeAllFetches().fetchReferenceEntityCollection(Room.class, "RoomToOrder").fetchReferenceEntityCollectionForInnerObject(RoomType.class, "RoomTypeToRoom").fetchChildEntityCollectionForInnerObject(Rate.class).fetchChildEntityCollectionForInnerObject(Price.class).closeAllFetches().fetchChildEntityCollection(JournalRecord.class).closeAllFetches().fetchReferenceEntityCollection(Category.class, "OrderToCategory").fetchChildEntityCollectionForInnerObject(Complimentary.class).fetchReferenceEntityCollectionForInnerObject(Maintenance.class, "MaintenanceToComplimentary").closeAllFetches().addCondition(PriceEqualsToRoomCondition.class).getEntityCollection();
        } catch (ResultEntityNullException e) {
            return null;
        }
    }

    @Override
    public int insertOrder(Order order) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObjects, objectId, null, 2, null, null);                      //2 = Order

            jdbcTemplate.update(insertAttributes, 5, objectId, objectId, null);
            jdbcTemplate.update(insertAttributes, 8, objectId, null, order.getRegistrationDate());     //Registration_date
            jdbcTemplate.update(insertAttributes, 9, objectId, order.getIsPaidFor(), null);          //Is_paid_for
            jdbcTemplate.update(insertAttributes, 10, objectId, order.getIsConfirmed(), null);       //Is_confirmed
            jdbcTemplate.update(insertAttributes, 11, objectId, null, order.getLivingStartDate());     //Living_start_date
            jdbcTemplate.update(insertAttributes, 12, objectId, null, order.getLivingFinishDate());    //Living_finish_date
            jdbcTemplate.update(insertAttributes, 13, objectId, order.getSum(), null);               //Sum
            jdbcTemplate.update(insertAttributes, 14, objectId, order.getComment(), null);           //Comment

            jdbcTemplate.update(insertObjReference, 6, objectId, order.getRoom().getObjectId());     //Booked
            jdbcTemplate.update(insertObjReference, 7, objectId, order.getClient().getObjectId());     //Belong
        } catch (NullPointerException e) {
            throw new TransactionException(order);
        }
        return objectId;
    }

    /**
     * Метод получает новый заказ и старый, затем сравнивает их поля и выполняет запросы по шаблону
     */

    @Override
    public void updateOrder(Order newOrder, Order oldOrder) throws TransactionException {
        try {
            if (oldOrder.getRegistrationDate().getTime() != newOrder.getRegistrationDate().getTime()) {
                jdbcTemplate.update(updateAttributes, null,  newOrder.getRegistrationDate(), newOrder.getObjectId(), 8);
            }
            if (!oldOrder.getIsPaidFor().equals(newOrder.getIsPaidFor())) {
                jdbcTemplate.update(updateAttributes, newOrder.getIsPaidFor(), null, newOrder.getObjectId(), 9);
            }
            if (!oldOrder.getIsConfirmed().equals(newOrder.getIsConfirmed())) {
                jdbcTemplate.update(updateAttributes, newOrder.getIsConfirmed(), null, newOrder.getObjectId(), 10);
            }
            if (oldOrder.getLivingStartDate().getTime() != newOrder.getLivingStartDate().getTime()) {
                jdbcTemplate.update(updateAttributes, null, newOrder.getLivingStartDate(), newOrder.getObjectId(), 11);
            }
            if (oldOrder.getLivingFinishDate().getTime() != newOrder.getLivingFinishDate().getTime()) {
                jdbcTemplate.update(updateAttributes, null,  newOrder.getLivingFinishDate(), newOrder.getObjectId(), 12);
            }
            if (!oldOrder.getSum().equals(newOrder.getSum())) {
                jdbcTemplate.update(updateAttributes, newOrder.getSum(), null, newOrder.getObjectId(), 13);
            }
            if (!oldOrder.getComment().equals(newOrder.getComment())) {
                jdbcTemplate.update(updateAttributes, newOrder.getComment(), null, newOrder.getObjectId(), 14);
            }
            if (oldOrder.getRoom().getObjectId() != newOrder.getRoom().getObjectId()) {
                jdbcTemplate.update(updateReference, newOrder.getRoom().getObjectId(), newOrder.getObjectId(), 6);
            }
            if (oldOrder.getClient().getObjectId() != newOrder.getClient().getObjectId()) {
                jdbcTemplate.update(updateReference, newOrder.getClient().getObjectId(), newOrder.getObjectId(), 7);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newOrder);
        }
    }

    @Override
    public int deleteOrder(int id) {
        return deleteSingleEntityById(id);
    }
}
