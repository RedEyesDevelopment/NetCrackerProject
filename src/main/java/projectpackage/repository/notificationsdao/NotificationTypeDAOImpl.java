package projectpackage.repository.notificationsdao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.support.daoexceptions.*;
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
                    .fetchRootReference(Role.class, "RoleToNotificationType")
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
    public Integer insertNotificationType(NotificationType notificationType) throws TransactionException {
        if (notificationType == null) return null;
        Integer objectId = nextObjectId();
        try {
            jdbcTemplate.update(INSERT_OBJECT, objectId, null, 11, null, null);
            insertNotificationTypeTitle(objectId, notificationType);
            insertOrientedRole(objectId, notificationType);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return objectId;
    }

    @Override
    public Integer updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType)
            throws TransactionException {
        if (oldNotificationType == null || newNotificationType == null) return null;
        try {
            updateTitle(newNotificationType, oldNotificationType);
            updateRole(newNotificationType, oldNotificationType);
        } catch (DataIntegrityViolationException e) {
            throw new TransactionException(this, e.getMessage());
        }
        return newNotificationType.getObjectId();
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

    private void insertNotificationTypeTitle(Integer objectId, NotificationType notificationType) {
        if (notificationType.getNotificationTypeTitle() != null && !notificationType.getNotificationTypeTitle().isEmpty()) {
            jdbcTemplate.update(INSERT_ATTRIBUTE, 40, objectId, notificationType.getNotificationTypeTitle(), null);
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void insertOrientedRole(Integer objectId, NotificationType notificationType) {
        if (notificationType.getOrientedRole() != null) {
            jdbcTemplate.update(INSERT_OBJ_REFERENCE, 41, objectId, notificationType.getOrientedRole().getObjectId());
        } else {
            throw new RequiredFieldAbsenceException();
        }
    }

    private void updateRole(NotificationType newNotificationType, NotificationType oldNotificationType) {
        if (oldNotificationType.getOrientedRole() != null && newNotificationType.getOrientedRole() != null) {
            if (oldNotificationType.getOrientedRole().getObjectId() != newNotificationType.getOrientedRole().getObjectId()) {
                jdbcTemplate.update(UPDATE_REFERENCE, newNotificationType.getOrientedRole().getObjectId(),
                        newNotificationType.getObjectId(), 41);
            }
        } else {
            throw new RequiredFieldAbsenceException();
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
            throw new RequiredFieldAbsenceException();
        }
    }
}
