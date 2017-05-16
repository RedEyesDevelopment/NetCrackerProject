package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.reacdao.exceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationTypeDAO {
    public void insertNotificationType(NotificationType notificationType) throws TransactionException;
    public void updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) throws TransactionException;
}
