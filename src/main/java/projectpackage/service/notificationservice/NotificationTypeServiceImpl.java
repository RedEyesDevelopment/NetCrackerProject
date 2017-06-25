package projectpackage.service.notificationservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.notificationsdao.NotificationTypeDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {

    private static final Logger LOGGER = Logger.getLogger(NotificationTypeServiceImpl.class);

    @Autowired
    NotificationTypeDAO notificationTypeDAO;

    @Override
    public List<NotificationType> getAllNotificationTypes() {
        List<NotificationType> notificationTypes = notificationTypeDAO.getAllNotificationTypes();
        if (notificationTypes == null) LOGGER.info("Returned NULL!!!");
        return notificationTypes;
    }

    @Override
    public List<NotificationType> getAllNotificationTypes(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<NotificationType> getNotificationTypeByRole(Role role) {
        List<NotificationType> answer = new ArrayList<>();
        List<NotificationType> allNotificationTypes = getAllNotificationTypes();
        for (NotificationType notificationType : allNotificationTypes) {
            if (notificationType.getOrientedRole().equals(role)) {
                answer.add(notificationType);
            }
        }
        return answer;
    }

    @Override
    public NotificationType getSingleNotificationTypeById(Integer id) {
        NotificationType notificationType = notificationTypeDAO.getNotificationType(id);
        if (notificationType == null) LOGGER.info("Returned NULL!!!");
        return notificationType;
    }

    @Override
    public IUDAnswer deleteNotificationType(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            notificationTypeDAO.deleteNotificationType(id);
        } catch (ReferenceBreakException e) {
            return notificationTypeDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            return notificationTypeDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            return notificationTypeDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            return notificationTypeDAO.rollback(id, NULL_ID, e);
        }
        notificationTypeDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertNotificationType(NotificationType notificationType) {
        if (notificationType == null) return null;
        Integer notifTypeId = null;
        try {
            notifTypeId = notificationTypeDAO.insertNotificationType(notificationType);
        } catch (IllegalArgumentException e) {
            return notificationTypeDAO.rollback(WRONG_FIELD, e);
        }
        notificationTypeDAO.commit();
        return new IUDAnswer(notifTypeId,true);
    }

    @Override
    public IUDAnswer updateNotificationType(Integer id, NotificationType newNotificationType) {
        if (newNotificationType == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            newNotificationType.setObjectId(id);
            NotificationType oldNotificationType = notificationTypeDAO.getNotificationType(id);
            notificationTypeDAO.updateNotificationType(newNotificationType, oldNotificationType);
        } catch (IllegalArgumentException e) {
            return notificationTypeDAO.rollback(WRONG_FIELD, e);
        }
        notificationTypeDAO.commit();
        return new IUDAnswer(id,true);
    }
}
