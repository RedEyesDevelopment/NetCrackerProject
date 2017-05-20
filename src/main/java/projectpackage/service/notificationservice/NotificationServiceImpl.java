package projectpackage.service.notificationservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.notificationsdao.NotificationDAO;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationServiceImpl implements NotificationService{

    private static final Logger LOGGER = Logger.getLogger(NotificationServiceImpl.class);

    @Autowired
    NotificationDAO notificationDAO;

    @Override
    public List<Notification> getAllNotifications(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = notificationDAO.getAllNotifications();
        if (notifications == null) LOGGER.info("Returned NULL!!!");
        return notifications;
    }

    @Override
    public List<Notification> getNotificationsBySendDate(NotificationType notificationType) {
        return null;
    }

    @Override
    public List<Notification> getNotificationsByExecutedDate(Date date) {
        return null;
    }

    @Override
    public List<Notification> getNotificationsByType(NotificationType notificationType) {
        return null;
    }

    @Override
    public List<Notification> getNotificationsByAuthor(User user) {
        return null;
    }

    @Override
    public List<Notification> getNotificationsByExecutor(User user) {
        return null;
    }

    @Override
    public List<Notification> getNotificationsForRole(Role role) {
        return null;
    }

    @Override
    public Notification getSingleNotificationById(int id) {
        Notification notification = notificationDAO.getNotification(id);
        if (notification == null) LOGGER.info("Returned NULL!!!");
        return notification;
    }

    @Override
    public boolean deleteNotification(int id) {
        int count = 0;
        count = count + notificationDAO.deleteNotification(id);
        if (count == 0) return false;
        else return true;
    }

    @Override
    public boolean insertNotification(Notification notification) {
        try {
            int notifId = notificationDAO.insertNotification(notification);
            LOGGER.info("Get from DB notificationId = " + notifId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateNotification(int id, Notification newNotification) {
        try {
            newNotification.setObjectId(id);
            Notification oldNotification = notificationDAO.getNotification(id);
            notificationDAO.updateNotification(newNotification, oldNotification);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
