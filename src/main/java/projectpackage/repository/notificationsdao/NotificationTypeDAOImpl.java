package projectpackage.repository.notificationsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.List;

@Repository
public class NotificationTypeDAOImpl extends AbstractDAO implements NotificationTypeDAO {
    private static final Logger LOGGER = Logger.getLogger(NotificationTypeDAOImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public NotificationType getNotificationType(Integer id) {
        if (id == null) {
            return null;
        }

        return (NotificationType) manager.createReactEAV(NotificationType.class)
                .fetchRootReference(Role.class, "RoleToNotificationType")
                .closeAllFetches().getSingleEntityWithId(id);
    }

    @Override
    public List<NotificationType> getAllNotificationTypes() {
        return (List<NotificationType>) manager.createReactEAV(NotificationType.class)
                .fetchRootReference(Role.class, "RoleToNotificationType")
                .closeAllFetches().getEntityCollection();
    }

    @Override
    public Integer insertNotificationType(NotificationType notificationType) {
        if (notificationType == null) {
            return null;
        }
        Integer objectId = nextObjectId();
        jdbcTemplate.update(INSERT_OBJECT, objectId, null, 11, null, null);
        insertNotificationTypeTitle(objectId, notificationType);
        insertOrientedRole(objectId, notificationType);

        return objectId;
    }

    @Override
    public Integer updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) {
        if (oldNotificationType == null || newNotificationType == null) {
            return null;
        }

        updateTitle(newNotificationType, oldNotificationType);
        updateRole(newNotificationType, oldNotificationType);

        return newNotificationType.getObjectId();
    }

    @Override
    public void deleteNotificationType(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        NotificationType notificationType = null;
        try {
            notificationType = getNotificationType(id);
        } catch (ClassCastException e) {
            throw new WrongEntityIdException(this, e.getMessage());
        }
        if (null == notificationType) {
            throw new DeletedObjectNotExistsException(this);
        }

        deleteSingleEntityById(id);
    }

    private void insertNotificationTypeTitle(Integer objectId, NotificationType notificationType) {
        if (notificationType.getNotificationTypeTitle() != null && !notificationType.getNotificationTypeTitle().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 40, objectId, notificationType.getNotificationTypeTitle(), null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void insertOrientedRole(Integer objectId, NotificationType notificationType) {
        if (notificationType.getOrientedRole() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 41, objectId, notificationType.getOrientedRole().getObjectId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateRole(NotificationType newNotificationType, NotificationType oldNotificationType) {
        if (oldNotificationType.getOrientedRole() != null && newNotificationType.getOrientedRole() != null) {
            if (oldNotificationType.getOrientedRole().getObjectId() != newNotificationType.getOrientedRole().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotificationType.getOrientedRole().getObjectId(),
                        newNotificationType.getObjectId(), 41);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void updateTitle(NotificationType newNotificationType, NotificationType oldNotificationType) {
        if (oldNotificationType.getNotificationTypeTitle() != null && newNotificationType.getNotificationTypeTitle() != null
                && !newNotificationType.getNotificationTypeTitle().isEmpty()) {
            if (!oldNotificationType.getNotificationTypeTitle().equals(newNotificationType.getNotificationTypeTitle())) {
                jdbcTemplate.update(UPDATE_ATTRIBUTE, newNotificationType.getNotificationTypeTitle(), null,
                        newNotificationType.getObjectId(), 40);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
