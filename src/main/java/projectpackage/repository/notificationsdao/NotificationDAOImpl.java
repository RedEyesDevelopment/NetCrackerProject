package projectpackage.repository.notificationsdao;

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
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Repository
public class NotificationDAOImpl extends AbstractDAO implements NotificationDAO {
    private static final Logger LOGGER = Logger.getLogger(NotificationDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Notification> getAllNotificationsForInMemoryService() {
        List<Notification> notifs = null;
        try {
            notifs = (List<Notification>) manager.createReactEAV(Notification.class).fetchRootReference(NotificationType.class, "NotificationTypeToNotification").fetchInnerReference(Role.class,"RoleToNotificationType").closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
        return notifs;
    }

    @Override
    public Notification getNotification(Integer id) {
        if (id == null) return null;
        try {
            return (Notification) manager.createReactEAV(Notification.class)
                    .fetchRootReference(User.class, "UserToNotificationAsAuthor")
                    .fetchInnerReference(Role.class, "RoleToUser").closeFetch()
                    .fetchInnerChild(Phone.class)
                    .closeAllFetches()
                    .fetchRootReference(NotificationType.class, "NotificationTypeToNotification")
                    .fetchInnerReference(Role.class, "RoleToNotificationType").closeAllFetches()
                    .fetchRootReference(Order.class, "OrderToNotification")
                    .fetchInnerChild(JournalRecord.class).fetchInnerReference(Maintenance.class, "MaintenanceToJournalRecord")
                    .closeFetch().closeFetch()
                    .fetchInnerReference(Room.class, "RoomToOrder")
                    .fetchInnerReference(RoomType.class, "RoomTypeToRoom").closeFetch().closeFetch()
                    .fetchInnerReference(Category.class, "OrderToCategory")
                    .fetchInnerChild(Complimentary.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary").closeAllFetches()
                    .fetchRootReference(User.class, "UserToNotificationAsExecutor")
                    .fetchInnerChild(Phone.class)
                    .closeAllFetches()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Notification> getAllNotifications() {
        try {
            return manager.createReactEAV(Notification.class)
                    .fetchRootReference(User.class, "UserToNotificationAsAuthor")
                    .fetchInnerChild(Phone.class).closeFetch()
                    .fetchInnerReference(Role.class, "RoleToUser").closeAllFetches()
                    .fetchRootReference(NotificationType.class, "NotificationTypeToNotification")
                    .fetchInnerReference(Role.class, "RoleToNotificationType").closeAllFetches()
                    .fetchRootReference(Order.class, "OrderToNotification")
                    .fetchInnerChild(JournalRecord.class).fetchInnerReference(Maintenance.class, "MaintenanceToJournalRecord")
                    .closeFetch().closeFetch()
                    .fetchInnerReference(Room.class, "RoomToOrder")
                    .fetchInnerReference(RoomType.class, "RoomTypeToRoom").closeFetch().closeFetch()
                    .fetchInnerReference(Category.class, "OrderToCategory")
                    .fetchInnerChild(Complimentary.class)
                    .fetchInnerReference(Maintenance.class, "MaintenanceToComplimentary").closeAllFetches()
                    .fetchRootReference(User.class, "UserToNotificationAsExecutor")
                    .fetchInnerChild(Phone.class)
                    .closeAllFetches()
                    .getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertNotification(Notification notification) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(INSERT_OBJECT, objectId, null, 4, null, null);

            jdbcTemplate.update(INSERT_ATTRIBUTE, 22, objectId, notification.getMessage(), null);
            jdbcTemplate.update(INSERT_ATTRIBUTE, 23, objectId, null, notification.getSendDate());
            jdbcTemplate.update(INSERT_ATTRIBUTE, 25, objectId, null, notification.getExecutedDate());

            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 21, objectId, notification.getAuthor().getObjectId());
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 26, objectId, notification.getNotificationType().getObjectId());
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 24, objectId, notification.getExecutedBy().getObjectId());
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 27, objectId, notification.getOrder().getObjectId());
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public void updateNotification(Notification newNotification, Notification oldNotification) throws TransactionException {
        try {
            if (!oldNotification.getMessage().equals(newNotification.getMessage())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newNotification.getMessage(), null,
                        newNotification.getObjectId(), 22);
            }
            if (oldNotification.getSendDate().getTime() != newNotification.getSendDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newNotification.getSendDate(),
                        newNotification.getObjectId(), 23);
            }
            if (oldNotification.getExecutedDate().getTime() != newNotification.getExecutedDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newNotification.getExecutedDate(),
                        newNotification.getObjectId(), 25);
            }
            if (oldNotification.getAuthor().getObjectId() != newNotification.getAuthor().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotification.getAuthor().getObjectId(),
                        newNotification.getObjectId(), 21);
            }
            if (oldNotification.getNotificationType().getObjectId() != newNotification.getNotificationType().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotification.getNotificationType().getObjectId(),
                        newNotification.getObjectId(), 26);
            }
            if (oldNotification.getExecutedBy().getObjectId() != newNotification.getExecutedBy().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotification.getExecutedBy().getObjectId(),
                        newNotification.getObjectId(), 24);
            }
            if (oldNotification.getOrder().getObjectId() != newNotification.getOrder().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotification.getOrder().getObjectId(),
                        newNotification.getObjectId(), 27);
            }
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deleteNotification(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        Notification notification = null;
        try {
            notification = getNotification(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == notification) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
