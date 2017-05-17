package projectpackage.repository.notificationsdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationTypeDAOImpl extends AbstractDAO implements NotificationTypeDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int insertNotificationType(NotificationType notificationType) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            //11 = Notification_type
            jdbcTemplate.update(insertObjects, objectId, null, 11, null, null);
            //40 = Notification_type_title
            jdbcTemplate.update(insertAttributes, 40, objectId, notificationType.getNotificationTypeTitle(), null);
            //41 = Oriented
            jdbcTemplate.update(insertObjReference, 41, objectId, notificationType.getOrientedRole().getObjectId());
        } catch (NullPointerException e) {
            throw new TransactionException(notificationType);
        }
        return objectId;
    }

    @Override
    public void updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) throws TransactionException {
        try {
            if (!oldNotificationType.getNotificationTypeTitle().equals(newNotificationType.getNotificationTypeTitle())) {
                jdbcTemplate.update(updateAttributes, newNotificationType.getNotificationTypeTitle(), null,
                        newNotificationType.getObjectId(), 40);
            }
            if (oldNotificationType.getOrientedRole().getObjectId() != newNotificationType.getOrientedRole().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotificationType.getOrientedRole().getObjectId(),
                        newNotificationType.getObjectId(), 41);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(newNotificationType);
        }
    }

    @Override
    public int deleteNotificationType(int id) {
        return deleteSingleEntityById(id);
    }
}
