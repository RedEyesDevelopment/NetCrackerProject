package projectpackage.service.notificationservice;

import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;

import java.util.List;

public interface NotificationTypeService extends MessageBook{
    public List<NotificationType> getNotificationTypeByRole(Role role);
    public List<NotificationType> getAllNotificationTypes();
    public NotificationType getSingleNotificationTypeById(Integer id);
    public IUDAnswer deleteNotificationType(Integer id);
    public IUDAnswer insertNotificationType(NotificationType notificationType);
    public IUDAnswer updateNotificationType(Integer id, NotificationType newNotificationType);
}
