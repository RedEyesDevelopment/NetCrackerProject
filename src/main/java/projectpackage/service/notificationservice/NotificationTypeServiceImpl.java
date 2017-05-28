package projectpackage.service.notificationservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.notificationsdao.NotificationTypeDAO;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {

    private static final Logger LOGGER = Logger.getLogger(NotificationTypeServiceImpl.class);

    @Autowired
    NotificationTypeDAO notificationTypeDAO;

    @Override
    public List<NotificationType> getAllNotificationTypes() {
        List<NotificationType> notificationTypes = notificationTypeDAO.getAllNotificationTypes();
        if (notificationTypes == null) LOGGER.info("Returned NULL!!!");
        return notificationTypes;
    }

    @Override
    public List<NotificationType> getAllNotificationTypes(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<NotificationType> getNotificationTypeByRole(Role role) {
        return null;
    }

    @Override
    public NotificationType getSingleNotificationTypeById(int id) {
        NotificationType notificationType = notificationTypeDAO.getNotificationType(id);
        if (notificationType == null) LOGGER.info("Returned NULL!!!");
        return notificationType;
    }

    @Override
    public IUDAnswer deleteNotificationType(int id) {
        try {
            notificationTypeDAO.deleteNotificationType(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(id,false, e.printReferencesEntities());
        }
        return new IUDAnswer(id,true);
    }

    @Override
    public IUDAnswer insertNotificationType(NotificationType notificationType) {
        Integer notifTypeId = null;
        try {
            notifTypeId = notificationTypeDAO.insertNotificationType(notificationType);
            LOGGER.info("Get from DB notificationTypeId = " + notifTypeId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(notifTypeId,false, e.getMessage());
        }
        return new IUDAnswer(notifTypeId,true);
    }

    @Override
    public IUDAnswer updateNotificationType(int id, NotificationType newNotificationType) {
        try {
            newNotificationType.setObjectId(id);
            NotificationType oldNotificationType = notificationTypeDAO.getNotificationType(id);
            notificationTypeDAO.updateNotificationType(newNotificationType, oldNotificationType);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, e.getMessage());
        }
        return new IUDAnswer(id,true);
    }
}
