package projectpackage.repository.notificationsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

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
            return (NotificationType) manager.createReactEAV(NotificationType.class)
                    .fetchRootReference(Role.class, "RoleToNotificationType")
                    .closeAllFetches().getSingleEntityWithId(id);
    }

    @Override
    public List<NotificationType> getAllNotificationTypes() {
            return (List<NotificationType>) manager.createReactEAV(NotificationType.class)
                    .fetchRootReference(Role.class, "RoleToNotification")
                    .closeAllFetches().getEntityCollection();
    }

    @Override
    public int insertNotificationType(NotificationType notificationType) throws TransactionException {
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(insertObject, objectId, null, 11, null, null);

            jdbcTemplate.update(insertAttribute, 40, objectId, notificationType.getNotificationTypeTitle(), null);

            jdbcTemplate.update(insertObjReference, 41, objectId, notificationType.getOrientedRole().getObjectId());
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
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
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
    }

    @Override
    public void deleteNotificationType(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException {
        NotificationType notificationType = null;
        try {
            notificationType = getNotificationType(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == notificationType) throw new DeletedObjectNotExistsException(this);

        deleteSingleEntityById(id);
    }
}
