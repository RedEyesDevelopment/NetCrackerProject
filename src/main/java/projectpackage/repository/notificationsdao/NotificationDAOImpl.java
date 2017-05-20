package projectpackage.repository.notificationsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.orders.Order;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationDAOImpl extends AbstractDAO implements NotificationDAO {
    private static final Logger LOGGER = Logger.getLogger(NotificationDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Notification getNotification(Integer id) {
        if (id == null) return null;
        try {
            return (Notification) manager.createReactEAV(Notification.class).fetchReferenceEntityCollection(User.class, "UserToNotificationAsAuthor").closeAllFetches().fetchReferenceEntityCollection(NotificationType.class, "NotificationTypeToNotification").closeAllFetches().fetchReferenceEntityCollection(Order.class, "OrderToNotification").closeAllFetches().fetchReferenceEntityCollection(User.class, "UserToNotificationAsExecutor").closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<Notification> getAllNotifications() {
        try {
            return manager.createReactEAV(Notification.class).fetchReferenceEntityCollection(User.class, "UserToNotificationAsAuthor").closeAllFetches().fetchReferenceEntityCollection(NotificationType.class, "NotificationTypeToNotification").closeAllFetches().fetchReferenceEntityCollection(Order.class, "OrderToNotification").closeAllFetches().fetchReferenceEntityCollection(User.class, "UserToNotificationAsExecutor").closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertNotification(Notification notification) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObjects, objectId, null, 4, null, null);

            jdbcTemplate.update(insertAttributes, 22, objectId, notification.getMessage(), null);
            jdbcTemplate.update(insertAttributes, 23, objectId, null, notification.getSendDate());
            jdbcTemplate.update(insertAttributes, 25, objectId, null, notification.getExecutedDate());

            jdbcTemplate.update(insertObjReference, 21, objectId, notification.getAuthor().getObjectId());
            jdbcTemplate.update(insertObjReference, 26, objectId, notification.getNotificationType().getObjectId());
            jdbcTemplate.update(insertObjReference, 24, objectId, notification.getExecutedBy().getObjectId());
            jdbcTemplate.update(insertObjReference, 27, objectId, notification.getOrder().getObjectId());
        } catch (NullPointerException e) {
            throw new TransactionException(notification);
        }
        return objectId;
    }

    @Override
    public void updateNotification(Notification newNotification, Notification oldNotification) throws TransactionException {
        try {
            if (!oldNotification.getMessage().equals(newNotification.getMessage())) {
                jdbcTemplate.update(updateAttributes, newNotification.getMessage(), null,
                        newNotification.getObjectId(), 22);
            }
            if (!oldNotification.getSendDate().equals(newNotification.getSendDate())) {
                jdbcTemplate.update(updateAttributes, null, newNotification.getSendDate(),
                        newNotification.getObjectId(), 23);
            }
            if (!oldNotification.getExecutedDate().equals(newNotification.getExecutedDate())) {
                jdbcTemplate.update(updateAttributes, null, newNotification.getExecutedDate(),
                        newNotification.getObjectId(), 25);
            }
            if (oldNotification.getAuthor().getObjectId() != newNotification.getAuthor().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotification.getAuthor().getObjectId(),
                        newNotification.getObjectId(), 21);
            }
            if (oldNotification.getNotificationType().getObjectId() != newNotification.getNotificationType().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotification.getNotificationType().getObjectId(),
                        newNotification.getObjectId(), 26);
            }
            if (oldNotification.getExecutedBy().getObjectId() != newNotification.getExecutedBy().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotification.getExecutedBy().getObjectId(),
                        newNotification.getObjectId(), 24);
            }
            if (oldNotification.getOrder().getObjectId() != newNotification.getOrder().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotification.getOrder().getObjectId(),
                        newNotification.getObjectId(), 27);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newNotification);
        }
    }

    @Override
    public int deleteNotification(int id) {
        return deleteSingleEntityById(id);
    }
}
