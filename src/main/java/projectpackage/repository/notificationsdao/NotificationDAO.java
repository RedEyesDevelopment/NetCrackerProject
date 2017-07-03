package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.Notification;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationDAO extends Commitable, Rollbackable{
    public List<Notification> getAllNotExecutedNotifications();
    public Notification getNotification(Integer id);
    public List<Notification> getAllNotifications();
    public Integer insertNotification(Notification notification);
    public Integer updateNotification(Notification newNotification, Notification oldNotification);
    public void deleteNotification(Integer id);
}
