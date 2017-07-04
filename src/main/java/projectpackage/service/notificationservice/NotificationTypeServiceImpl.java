package projectpackage.service.notificationservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.notificationsdao.NotificationTypeDAO;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {

    private static final Logger LOGGER = Logger.getLogger(NotificationTypeServiceImpl.class);

    @Autowired
    NotificationTypeDAO notificationTypeDAO;

    @Transactional(readOnly = true)
    @Override
    public List<NotificationType> getAllNotificationTypes() {
        List<NotificationType> notificationTypes = notificationTypeDAO.getAllNotificationTypes();
        if (notificationTypes == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return notificationTypes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<NotificationType> getNotificationTypeByRole(Role role) {
        List<NotificationType> answer = new ArrayList<>();
        List<NotificationType> allNotificationTypes = getAllNotificationTypes();
        for (NotificationType notificationType : allNotificationTypes) {
            if (notificationType.getOrientedRole().equals(role)) {
                answer.add(notificationType);
            }
        }
        return answer;
    }

    @Transactional(readOnly = true)
    @Override
    public NotificationType getSingleNotificationTypeById(Integer id) {
        NotificationType notificationType = notificationTypeDAO.getNotificationType(id);
        if (notificationType == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return notificationType;
    }

    @Transactional
    @Override
    public IUDAnswer deleteNotificationType(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        notificationTypeDAO.deleteNotificationType(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertNotificationType(NotificationType notificationType) {
        if (notificationType == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        Integer notifTypeId = null;

        notifTypeId = notificationTypeDAO.insertNotificationType(notificationType);

        return new IUDAnswer(notifTypeId,true);
    }

    @Transactional
    @Override
    public IUDAnswer updateNotificationType(Integer id, NotificationType newNotificationType) {
        if (newNotificationType == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }

        newNotificationType.setObjectId(id);
        NotificationType oldNotificationType = notificationTypeDAO.getNotificationType(id);
        notificationTypeDAO.updateNotificationType(newNotificationType, oldNotificationType);


        return new IUDAnswer(id,true);
    }
}
