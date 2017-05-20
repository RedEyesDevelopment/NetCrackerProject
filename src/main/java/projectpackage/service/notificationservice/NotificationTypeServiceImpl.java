package projectpackage.service.notificationservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.notificationsdao.NotificationDAO;
import projectpackage.repository.notificationsdao.NotificationTypeDAO;

import java.util.Iterator;
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

    @Autowired
    NotificationDAO notificationDAO;

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
        return null;
    }

    @Override
    public NotificationType getSingleNotificationTypeById(int id) {
        NotificationType notificationType = notificationTypeDAO.getNotificationType(id);
        if (notificationType == null) LOGGER.info("Returned NULL!!!");
        return notificationType;
    }

    @Override
    public boolean deleteNotificationType(int id) {
        int count = 0;
        List<Notification> list = notificationDAO.getAllNotifications();
        Iterator<Notification> iterator = list.iterator();
        while (iterator.hasNext()){
            Notification not = iterator.next();
            if (not.getNotificationType().getObjectId() == id){
                count += notificationDAO.deleteNotification(not.getObjectId());
            }
        }

        count += notificationTypeDAO.deleteNotificationType(id);
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
            NotificationType oldNotificationType = notificationTypeDAO.getNotificationType(id);
            notificationTypeDAO.updateNotificationType(newNotificationType, oldNotificationType);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
