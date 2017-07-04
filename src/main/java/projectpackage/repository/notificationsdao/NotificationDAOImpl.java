package projectpackage.repository.notificationsdao;

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
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.reacteav.conditions.ConditionExecutionMoment;
import projectpackage.repository.reacteav.conditions.StringWhereCondition;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.Date;
import java.util.List;

@Repository
public class NotificationDAOImpl extends AbstractDAO implements NotificationDAO {
    private static final Logger LOGGER = Logger.getLogger(NotificationDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Notification> getAllNotExecutedNotifications() {
        return manager.createReactEAV(Notification.class).addCondition(new StringWhereCondition("ATTRS3.DATE_VALUE IS NULL"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).fetchRootReference(NotificationType.class, "NotificationTypeToNotification").fetchInnerReference(Role.class, "RoleToNotificationType").closeAllFetches().fetchRootReference(User.class, "UserToNotificationAsAuthor").closeAllFetches().fetchRootReference(Order.class, "OrderToNotification").closeAllFetches().getEntityCollection();
    }

    @Override
    public Notification getNotExecutedNotification(Integer id) {
        return (Notification) manager.createReactEAV(Notification.class).addCondition(new StringWhereCondition("ATTRS3.DATE_VALUE IS NULL"), ConditionExecutionMoment.AFTER_APPENDING_WHERE).fetchRootReference(NotificationType.class, "NotificationTypeToNotification").fetchInnerReference(Role.class, "RoleToNotificationType").closeAllFetches().fetchRootReference(User.class, "UserToNotificationAsAuthor").closeAllFetches().fetchRootReference(Order.class, "OrderToNotification").closeAllFetches().getSingleEntityWithId(id);
    }

    @Override
    public Notification getNotification(Integer id) {
        if (id == null) {
            return null;
        }

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
    }

    @Override
    public List<Notification> getAllNotifications() {
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
    }

    @Override
    public Integer insertNotification(Notification notification) {
        if (notification == null) {
            return null;
        }
        Integer objectId = nextObjectId();
        jdbcTemplate.update(INSERT_OBJECT, objectId, null, 4, null, null);
        insertSendDate(objectId, notification);
        insertMessage(objectId, notification);
        insertAuthor(objectId, notification);
        insertNotificationType(objectId, notification);
        insertOrder(objectId, notification);
        insertExecutedByAndDate(objectId, notification);
        return objectId;
    }

    @Override
    public Integer updateNotification(Notification newNotification, Notification oldNotification) {
        if (oldNotification == null || newNotification == null) {
            return null;
        }

        updateMessage(newNotification, oldNotification);
        updateSendDate(newNotification, oldNotification);
        updateAuthor(newNotification, oldNotification);
        updateNotificationType(newNotification, oldNotification);
        updateOrder(newNotification, oldNotification);
        updateDateAndExecutedBy(newNotification, oldNotification);

        return newNotification.getObjectId();
    }

    @Override
    public void deleteNotification(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        Notification notification = null;
        try {
            notification = getNotExecutedNotification(id);
            if (notification == null) {
                notification = getNotification(id);
            }
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == notification) {
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }

    private void insertSendDate(Integer objectId, Notification notification) {
        if (notification.getSendDate() != null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 23, objectId, null, notification.getSendDate());
        } else {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 23, objectId, null, new Date());
        }
    }

    private void insertMessage(Integer objectId, Notification notification) {
        if (notification.getMessage() != null && !notification.getMessage().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 22, objectId, notification.getMessage(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertAuthor(Integer objectId, Notification notification) {
        if (notification.getAuthor() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 21, objectId, notification.getAuthor().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertNotificationType(Integer objectId, Notification notification) {
        if (notification.getNotificationType() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 26, objectId, notification.getNotificationType().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertOrder(Integer objectId, Notification notification) {
        if (notification.getOrder() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 27, objectId, notification.getOrder().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertExecutedByAndDate(Integer objectId, Notification notification) {
        if (notification.getExecutedBy() != null && notification.getExecutedDate() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 24, objectId, notification.getExecutedBy().getObjectId());
            jdbcTemplate.update(INSERT_ATTRIBUTE, 25, objectId, null, notification.getExecutedDate());
        } else if (notification.getExecutedBy() == null && notification.getExecutedDate() == null) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 25, objectId, null, null);
        }
    }

    private void updateMessage(Notification newNotification, Notification oldNotification) {
        if (oldNotification.getMessage() != null && newNotification.getMessage() != null
                && !newNotification.getMessage().isEmpty()) {
            if (!oldNotification.getMessage().equals(newNotification.getMessage())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newNotification.getMessage(), null,
                        newNotification.getObjectId(), 22);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateSendDate(Notification newNotification, Notification oldNotification) {
        if (oldNotification.getSendDate() != null && newNotification.getSendDate() != null) {
            if (oldNotification.getSendDate().getTime() != newNotification.getSendDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newNotification.getSendDate(),
                        newNotification.getObjectId(), 23);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateAuthor(Notification newNotification, Notification oldNotification) {
        if (oldNotification.getAuthor() != null && newNotification.getAuthor() != null) {
            if (oldNotification.getAuthor().getObjectId() != newNotification.getAuthor().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotification.getAuthor().getObjectId(),
                        newNotification.getObjectId(), 21);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateNotificationType(Notification newNotification, Notification oldNotification) {
        if (oldNotification.getNotificationType() != null && newNotification.getNotificationType() != null) {
            if (oldNotification.getNotificationType().getObjectId() != newNotification.getNotificationType().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotification.getNotificationType().getObjectId(),
                        newNotification.getObjectId(), 26);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateOrder(Notification newNotification, Notification oldNotification) {
        if (oldNotification.getOrder() != null && newNotification.getOrder() != null) {
            if (oldNotification.getOrder().getObjectId() != newNotification.getOrder().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotification.getOrder().getObjectId(),
                        newNotification.getObjectId(), 27);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateDateAndExecutedBy(Notification newNotification, Notification oldNotification) {
        if ((newNotification.getExecutedBy() == null && newNotification.getExecutedDate() != null)
                || (newNotification.getExecutedBy() != null && newNotification.getExecutedDate() == null)) {
            throw new IllegalArgumentException();
        }
        if (oldNotification.getExecutedBy() == null && newNotification.getExecutedBy() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 24,
                    newNotification.getObjectId(), newNotification.getExecutedBy().getObjectId());
            jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newNotification.getExecutedDate(),
                    newNotification.getObjectId(), 25);
        }
        if (oldNotification.getExecutedBy() != null && newNotification.getExecutedBy() != null) {
            if (oldNotification.getExecutedBy().getObjectId() != newNotification.getExecutedBy().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotification.getExecutedBy().getObjectId(),
                        newNotification.getObjectId(), 24);
            }
            if (oldNotification.getExecutedDate().getTime() != newNotification.getExecutedDate().getTime()) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, null, newNotification.getExecutedDate(),
                        newNotification.getObjectId(), 25);
            }
        }
        if (oldNotification.getExecutedBy() != null && newNotification.getExecutedBy() == null) {
            jdbcTemplate.update(DELETE_REFERENCE, oldNotification.getObjectId(),
                    24, oldNotification.getExecutedBy().getObjectId());
            jdbcTemplate.update(UPDATE_ATTRIBUTE, null, null,
                    newNotification.getObjectId(), 25);
        }
    }
}
