package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.daoexceptions.WrongEntityIdException;
import projectpackage.repository.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface NotificationTypeDAO {
    public NotificationType getNotificationType(Integer id);
    public List<NotificationType> getAllNotificationTypes();
    public int insertNotificationType(NotificationType notificationType) throws TransactionException;
    public void updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) throws TransactionException;
    public void deleteNotificationType(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
