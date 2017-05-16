package projectpackage.service.notificationservice;

import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationServiceImpl implements NotificationService{
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
        return false;
    }

    @Override
    public boolean insertNotification(Notification notification) {
        return false;
    }

    @Override
    public boolean updateNotification(Notification newNotification) {
        return false;
    }
}
