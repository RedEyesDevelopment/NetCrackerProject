package projectpackage.service.notificationservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.notificationsdao.NotificationTypeDAO;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationTypeServiceImpl implements NotificationTypeService{

    private static final Logger LOGGER = Logger.getLogger(NotificationTypeServiceImpl.class);

    @Autowired
    ReactEAVManager manager;

    @Autowired
    NotificationTypeDAO notificationTypeDAO;

    @Override
    public List<NotificationType> getAllNotificationTypes() {
        List<NotificationType> list = null;
        list = (List<NotificationType>) manager.createReactEAV(NotificationType.class)
                .fetchInnerEntityCollection(Role.class).closeFetch();
        return list;
    }

    @Override
    public List<NotificationType> getAllNotificationTypes(String orderingParameter, boolean ascend) {
        List<NotificationType> list = null;
        try {
            list = (List<NotificationType>) manager.createReactEAV(NotificationType.class)
                    .fetchInnerEntityCollection(Role.class).closeFetch()
                    .getEntityCollectionOrderByParameter(orderingParameter, ascend);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("getAllUsers method returned null list", e);
        }
        return list;
    }

    @Override
    //todo make getNotificationTypeByRole
    public List<NotificationType> getNotificationTypeByRole(Role role) {
        return null;
    }

    @Override
    public NotificationType getSingleNotificationTypeById(int id) {
        NotificationType notificationType = null;
        try {
            notificationType = (NotificationType) manager.createReactEAV(NotificationType.class)
                    .fetchInnerEntityCollection(Role.class).closeFetch()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("getSingleUserById method returned null list", e);
        }
        return notificationType;
    }

    @Override
    public boolean deleteNotificationType(int id) {
        return false;
    }

    @Override
    public boolean insertNotificationType(NotificationType notificationType) {
        try {
            int notifTypeId = notificationTypeDAO.insertNotificationType(notificationType);
            LOGGER.info("Get from DB notificationTypeId = " + notifTypeId);
        } catch (TransactionException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateNotificationType(int id, NotificationType newNotificationType) {
        return false;
    }
}
