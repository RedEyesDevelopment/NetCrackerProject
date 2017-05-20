package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationTypeDAO {
    public NotificationType getNotificationType(Integer id);
    public List<NotificationType> getAllNotificationTypes();
    public int insertNotificationType(NotificationType notificationType) throws TransactionException;
    public void updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) throws TransactionException;
    public int deleteNotificationType(int id);
}
