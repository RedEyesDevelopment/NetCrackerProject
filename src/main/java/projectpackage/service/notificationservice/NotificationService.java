package projectpackage.service.notificationservice;

import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.dto.IUDAnswer;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationService {
    public List<Notification> getNotificationsBySendDate(Date date);
    public List<Notification> getNotificationsByExecutedDate(Date date);
    public List<Notification> getNotificationsByType(NotificationType notificationType);
    public List<Notification> getNotificationsByAuthor(User user);
    public List<Notification> getNotificationsByExecutor(User user);
    public List<Notification> getNotificationsForRole(Role role);

    public List<Notification> getAllNotifications();
    public List<Notification> getAllNotifications(String orderingParameter, boolean ascend);
    public Notification getSingleNotificationById(int id);
    public IUDAnswer deleteNotification(int id);
    public IUDAnswer insertNotification(Notification notification);
    public IUDAnswer updateNotification(int id, Notification newNotification);
}
