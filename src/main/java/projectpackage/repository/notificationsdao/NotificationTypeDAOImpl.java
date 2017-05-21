package projectpackage.repository.notificationsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Repository
public class NotificationTypeDAOImpl extends AbstractDAO implements NotificationTypeDAO {
    private static final Logger LOGGER = Logger.getLogger(NotificationTypeDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public NotificationType getNotificationType(Integer id) {
        if (id == null) return null;
        try {
            return (NotificationType) manager.createReactEAV(NotificationType.class)
                    .fetchRootReference(Role.class, "RoleToNotification")
                    .closeAllFetches().getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public List<NotificationType> getAllNotificationTypes() {
        try {
            return (List<NotificationType>) manager.createReactEAV(NotificationType.class)
                    .fetchRootReference(Role.class, "RoleToNotification")
                    .closeAllFetches().getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn(e);
            return null;
        }
    }

    @Override
    public int insertNotificationType(NotificationType notificationType) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 11, null, null);

            jdbcTemplate.update(insertAttribute, 40, objectId, notificationType.getNotificationTypeTitle(), null);

            jdbcTemplate.update(insertObjReference, 41, objectId, notificationType.getOrientedRole().getObjectId());
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
        return objectId;
    }

    @Override
    public void updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) throws TransactionException {
        try {
            if (!oldNotificationType.getNotificationTypeTitle().equals(newNotificationType.getNotificationTypeTitle())) {
                jdbcTemplate.update(updateAttribute, newNotificationType.getNotificationTypeTitle(), null,
                        newNotificationType.getObjectId(), 40);
            }
            if (oldNotificationType.getOrientedRole().getObjectId() != newNotificationType.getOrientedRole().getObjectId()) {
                jdbcTemplate.update(updateReference, newNotificationType.getOrientedRole().getObjectId(),
                        newNotificationType.getObjectId(), 41);
            }
        } catch (NullPointerException e) {
            throw new TransactionException(this);
        }
    }

    @Override
    public void deleteNotificationType(int id) throws ReferenceBreakException {
        deleteSingleEntityById(id);
    }
}
