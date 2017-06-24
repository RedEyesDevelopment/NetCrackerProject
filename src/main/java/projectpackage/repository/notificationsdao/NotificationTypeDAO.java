package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.NotificationType;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationTypeDAO {
    public NotificationType getNotificationType(Integer id);
    public List<NotificationType> getAllNotificationTypes();
    public Integer insertNotificationType(NotificationType notificationType);
    public Integer updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType);
    public void deleteNotificationType(Integer id);
}
