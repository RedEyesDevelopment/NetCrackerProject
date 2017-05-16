package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.Notification;
import projectpackage.repository.reacdao.exceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationDAO {
    public void insertNotification(Notification notification) throws TransactionException;
    public void updateNotification(Notification newNotification, Notification oldNotification) throws TransactionException;
}
