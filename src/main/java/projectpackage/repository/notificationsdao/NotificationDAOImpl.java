package projectpackage.repository.notificationsdao;

import projectpackage.model.notifications.Notification;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationDAOImpl extends AbstractDAO implements NotificationDAO{
    @Override
    public int insertNotification(Notification notification) throws TransactionException {
        return 0;
    }

    @Override
    public void updateNotification(Notification newNotification, Notification oldNotification) throws TransactionException {

    }
}
