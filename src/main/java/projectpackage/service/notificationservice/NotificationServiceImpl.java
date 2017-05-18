package projectpackage.service.notificationservice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.notificationsdao.NotificationDAO;
import projectpackage.repository.reacteav.ReactEAVManager;
import projectpackage.repository.reacteav.exceptions.ResultEntityNullException;

import java.util.Date;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationServiceImpl implements NotificationService{

    private static final Logger LOGGER = Logger.getLogger(NotificationServiceImpl.class);

    @Autowired
    ReactEAVManager manager;

    @Autowired
    NotificationDAO notificationDAO;

    @Override
    public List<Notification> getAllNotifications(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return null;
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
        return null;
    }

    @Override
    public boolean deleteNotification(int id) {
        Notification notification = null;
        try {
            // todo тут собирать ReactEAV'ом и ниже проверить нет ли свзяей на него
        } catch (Throwable e) {
            LOGGER.warn("Problem with ReactEAV! Pls Check!", e);
            return false;
        }
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
        // todo ждём ReacEAV
        return false;
    }
}
