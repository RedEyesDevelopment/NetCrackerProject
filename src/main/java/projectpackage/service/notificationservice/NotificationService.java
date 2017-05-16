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
public interface NotificationService {
    public List<Notification> getNotificationsBySendDate(NotificationType notificationType);//TODO Denis
    public List<Notification> getNotificationsByExecutedDate(Date date);//TODO Denis
    public List<Notification> getNotificationsByType(NotificationType notificationType);//TODO Denis
    public List<Notification> getNotificationsByAuthor(User user);//TODO Denis
    public List<Notification> getNotificationsByExecutor(User user);//TODO Denis
    public List<Notification> getNotificationsForRole(Role role);//TODO Denis

    public List<Notification> getAllNotifications();//TODO Pacanu
    public List<Notification> getAllNotifications(String orderingParameter, boolean ascend);//TODO Pacanu
    public Notification getSingleNotificationById(int id);//TODO Pacanu
    public boolean deleteNotification(int id);//TODO Pacanu
    public boolean insertNotification(Notification notification);//TODO Pacanu
    public boolean updateNotification(Notification newNotification);//TODO Pacanu
}
