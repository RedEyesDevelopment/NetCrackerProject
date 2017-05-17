package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationTypeDAO {
    public int insertNotificationType(NotificationType notificationType) throws TransactionException;
    public void updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) throws TransactionException;
}
