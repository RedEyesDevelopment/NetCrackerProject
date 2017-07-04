package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface NotificationTypeDAO extends Commitable, Rollbackable{
    public NotificationType getNotificationType(Integer id);
    public List<NotificationType> getAllNotificationTypes();
    public Integer insertNotificationType(NotificationType notificationType);
    public Integer updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType);
    public void deleteNotificationType(Integer id);
}
