package projectpackage.service.notificationservice;

import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationTypeService {
    public List<NotificationType> getNotificationTypeByRole(Role role);//TODO Denis

    public List<NotificationType> getAllNotificationTypes();//TODO Pacanu
    public List<NotificationType> getAllNotificationTypes(String orderingParameter, boolean ascend);//TODO Pacanu
    public NotificationType getSingleNotificationTypeById(int id);//TODO Pacanu
    public boolean deleteNotificationType(int id);//TODO Pacanu
    public boolean insertNotificationType(NotificationType notificationType);//TODO Pacanu
    public boolean updateNotificationType(NotificationType newNotificationType);//TODO Pacanu
}
