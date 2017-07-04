package projectpackage.service.notificationservice;

import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.Date;
import java.util.List;

public interface NotificationService extends MessageBook{
    public List<Notification> getNotificationsBySendDate(Date date);
    public List<Notification> getNotificationsByExecutedDate(Date date);
    public List<Notification> getNotificationsByType(NotificationType notificationType);
    public List<Notification> getNotificationsByAuthor(User user);
    public List<Notification> getNotificationsByExecutor(User user);
    public List<Notification> getNotificationsForRole(Role role);

    public List<Notification> getAllNotExecutedNotifications();
    public List<Notification> getAllNotifications();
    public Notification getSingleNotificationById(Integer id);
    public Notification getNotExecutedNotificationById(Integer id);
    public IUDAnswer deleteNotification(Integer id);
    public IUDAnswer insertNotification(Notification notification);
    public IUDAnswer updateNotification(Integer id, Notification newNotification);
}
