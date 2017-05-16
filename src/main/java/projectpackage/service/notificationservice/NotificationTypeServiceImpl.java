package projectpackage.service.notificationservice;

import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationTypeServiceImpl implements NotificationTypeService{
    @Override
    public List<NotificationType> getAllNotificationTypes(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<NotificationType> getNotificationTypeByRole(Role role) {
        return null;
    }

    @Override
    public List<NotificationType> getAllNotificationTypes() {
        return null;
    }

    @Override
    public NotificationType getSingleNotificationTypeById(int id) {
        return null;
    }

    @Override
    public boolean deleteNotificationType(int id) {
        return false;
    }

    @Override
    public boolean insertNotificationType(NotificationType notificationType) {
        return false;
    }

    @Override
    public boolean updateNotificationType(NotificationType newNotificationType) {
        return false;
    }
}
