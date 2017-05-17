package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationTypeDAOImpl extends AbstractDAO implements NotificationTypeDAO{
    @Override
    public int insertNotificationType(NotificationType notificationType) throws TransactionException {
        return 0;
    }

    @Override
    public void updateNotificationType(NotificationType newNotificationType, NotificationType oldNotificationType) throws TransactionException {

    }

    @Override
    public int deleteNotificationType(int id) {
        return deleteSingleEntityById(id);
    }
}
