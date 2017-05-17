package projectpackage.repository.notificationsdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import projectpackage.model.notifications.Notification;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationDAOImpl extends AbstractDAO implements NotificationDAO{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int insertNotification(Notification notification) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObjects, objectId, null, 4, null, null);  //4 = Notification
            //22 = Message  23 = Send_date  25 = Executed_date
            jdbcTemplate.update(insertAttributes, 22, objectId, notification.getMessage(), null);
            jdbcTemplate.update(insertAttributes, 23, objectId, null, notification.getSendDate());
            jdbcTemplate.update(insertAttributes, 25, objectId, null, notification.getExecutedDate());
            //21 = Sent_by  26 = Has_notification_type  24 = Executed_by    27 = Consider
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
                jdbcTemplate.update(updateAttributes, newNotification.getMessage(), null, newNotification.getObjectId(), 22);
            }
            if (!oldNotification.getSendDate().equals(newNotification.getSendDate())) {
                jdbcTemplate.update(updateAttributes, null, newNotification.getSendDate(), newNotification
                        .getObjectId(), 23);
            }
            if (!oldNotification.getExecutedDate().equals(newNotification.getExecutedDate())) {
                jdbcTemplate.update(updateAttributes, null, newNotification.getExecutedDate(), newNotification
                        .getObjectId(), 25);
            }
            if (oldNotification.getAuthor().getObjectId() != newNotification.getAuthor().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotification.getAuthor().getObjectId(), newNotification
                        .getObjectId(), 21);
            }
            if (oldNotification.getNotificationType().getObjectId() != newNotification.getNotificationType().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotification.getNotificationType().getObjectId(), newNotification
                        .getObjectId(), 26);
            }
            if (oldNotification.getExecutedBy().getObjectId() != newNotification.getExecutedBy().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotification.getExecutedBy().getObjectId(), newNotification
                        .getObjectId(), 24);
            }
            if (oldNotification.getOrder().getObjectId() != newNotification.getOrder().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotification.getOrder().getObjectId(), newNotification
                        .getObjectId(), 27);
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
