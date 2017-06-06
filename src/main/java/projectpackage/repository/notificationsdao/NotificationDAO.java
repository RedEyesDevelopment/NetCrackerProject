package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.Notification;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationDAO {
    public List<Notification> getAllNotificationsForInMemoryService();
    public Notification getNotification(Integer id);
    public List<Notification> getAllNotifications();
    public int insertNotification(Notification notification) throws TransactionException;
    public void updateNotification(Notification newNotification, Notification oldNotification) throws TransactionException;
    public void deleteNotification(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
