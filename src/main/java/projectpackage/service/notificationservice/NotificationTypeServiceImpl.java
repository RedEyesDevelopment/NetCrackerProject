package projectpackage.service.notificationservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
@Log4j
@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {

    private static final Logger LOGGER = Logger.getLogger(NotificationTypeServiceImpl.class);

    @Autowired
    ReactEAVManager manager;

    @Autowired
    NotificationTypeDAO notificationTypeDAO;

    @Override
    public List<NotificationType> getAllNotificationTypes() {
        List<NotificationType> list = null;
        try {
            list = (List<NotificationType>) manager.createReactEAV(NotificationType.class)
                    .fetchInnerEntityCollection(Role.class).closeFetch()
                    .getEntityCollection();
        } catch (ResultEntityNullException e) {
            LOGGER.warn("getAllNotificationTypes method returned null list", e);
        }
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
            LOGGER.warn("getAllNotificationTypes method returned null list", e);
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
            LOGGER.warn("getSingleNotificationTypeById method returned null list", e);
        }
        return notificationType;
    }

    @Override
    public boolean deleteNotificationType(int id) {
        NotificationType notificationType = null;
        try {
            notificationType = (NotificationType) manager.createReactEAV(NotificationType.class)
                    .fetchInnerEntityCollection(Role.class).closeFetch()
                    .getSingleEntityWithId(id);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("Problem with ReactEAV! Pls Check!", e);
            return false;
        }
        int count = 0;
        /* todo надо проверить есть ли хоть один Notification этого типа
           написать этот кусок, когда будет готово getNotificationsByType */
        count = count + notificationTypeDAO.deleteNotificationType(id);
        if (count == 0) return false;
        else return true;
    }

    @Override
    public boolean insertNotificationType(NotificationType notificationType) {
        try {
            int notifTypeId = notificationTypeDAO.insertNotificationType(notificationType);
            LOGGER.info("Get from DB notificationTypeId = " + notifTypeId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateNotificationType(int id, NotificationType newNotificationType) {
        try {
            newNotificationType.setObjectId(id);
            NotificationType oldNotificationType = (NotificationType) manager.createReactEAV(NotificationType.class)
                    .fetchInnerEntityCollection(Role.class).closeFetch()
                    .getSingleEntityWithId(newNotificationType.getObjectId());
            notificationTypeDAO.updateNotificationType(newNotificationType, oldNotificationType);
        } catch (ResultEntityNullException e) {
            LOGGER.warn("Problem with ReactEAV! Pls Check!", e);
            return false;
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
