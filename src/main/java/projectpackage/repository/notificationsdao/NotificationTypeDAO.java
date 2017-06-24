package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationTypeDAO {
    public NotificationType getNotificationType(Integer id);
    public List<NotificationType> getAllNotificationTypes();
    public Integer insertNotificationType(NotificationType notificationType) throws TransactionException;
    public Integer updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) throws TransactionException;
    public void deleteNotificationType(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
