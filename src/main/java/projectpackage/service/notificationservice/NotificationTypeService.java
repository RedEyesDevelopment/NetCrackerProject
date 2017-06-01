package projectpackage.service.notificationservice;

import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.dto.IUDAnswer;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationTypeService {
    public List<NotificationType> getNotificationTypeByRole(Role role);

    public List<NotificationType> getAllNotificationTypes();
    public List<NotificationType> getAllNotificationTypes(String orderingParameter, boolean ascend);
    public NotificationType getSingleNotificationTypeById(int id);
    public IUDAnswer deleteNotificationType(int id);
    public IUDAnswer insertNotificationType(NotificationType notificationType);
    public IUDAnswer updateNotificationType(int id, NotificationType newNotificationType);
}
