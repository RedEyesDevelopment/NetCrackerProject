package projectpackage.service.notificationservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.notificationsdao.NotificationDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
@Log4j
@Service
public class NotificationServiceImpl implements NotificationService{

    private static final Logger LOGGER = Logger.getLogger(NotificationServiceImpl.class);

    @Autowired
    NotificationDAO notificationDAO;

    @Override
    public List<Notification> getAllNotifications(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = notificationDAO.getAllNotifications();
        if (notifications == null) LOGGER.info("Returned NULL!!!");
        return notifications;
    }

    @Override
    public List<Notification> getNotificationsBySendDate(Date date) {
        List<Notification> answer = new ArrayList<>();
        List<Notification> allNotifications = getAllNotifications();
        for (Notification notification : allNotifications) {
            if (notification.getSendDate().getTime() == date.getTime()) {
                answer.add(notification);
            }
        }
        return answer;
    }

    @Override
    public List<Notification> getNotificationsByExecutedDate(Date date) {
        List<Notification> answer = new ArrayList<>();
        List<Notification> allNotifications = getAllNotifications();
        for (Notification notification : allNotifications) {
            if (notification.getExecutedDate().getTime() == date.getTime()) {
                answer.add(notification);
            }
        }
        return answer;
    }

    @Override
    public List<Notification> getNotificationsByType(NotificationType notificationType) {
        List<Notification> answer = new ArrayList<>();
        List<Notification> allNotifications = getAllNotifications();
        for (Notification notification : allNotifications) {
            if (notification.getNotificationType().equals(notificationType)) {
                answer.add(notification);
            }
        }
        return answer;
    }

    @Override
    public List<Notification> getNotificationsByAuthor(User user) {
        List<Notification> answer = new ArrayList<>();
        List<Notification> allNotifications = getAllNotifications();
        for (Notification notification : allNotifications) {
            if (notification.getAuthor().equals(user)) {
                answer.add(notification);
            }
        }
        return answer;
    }

    @Override
    public List<Notification> getNotificationsByExecutor(User user) {
        List<Notification> answer = new ArrayList<>();
        List<Notification> allNotifications = getAllNotifications();
        for (Notification notification : allNotifications) {
            if (notification.getExecutedBy().equals(user)) {
                answer.add(notification);
            }
        }
        return answer;
    }

    @Override
    public List<Notification> getNotificationsForRole(Role role) {
        List<Notification> answer = new ArrayList<>();
        List<Notification> allNotifications = getAllNotifications();
        for (Notification notification : allNotifications) {
            if (notification.getNotificationType().getOrientedRole().equals(role)) {
                answer.add(notification);
            }
        }
        return answer;
    }

    @Override
    public List<Notification> getAllNotificationsForInMemoryService() {
        return notificationDAO.getAllNotificationsForInMemoryService();
    }

    @Override
    public Notification getSingleNotificationById(Integer id) {
        Notification notification = notificationDAO.getNotification(id);
        if (notification == null) LOGGER.info("Returned NULL!!!");
        return notification;
    }

    @Override
    public IUDAnswer deleteNotification(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            notificationDAO.deleteNotification(id);
        } catch (ReferenceBreakException e) {
            return notificationDAO.rollback(id, ON_ENTITY_REFERENCE, e);
        } catch (DeletedObjectNotExistsException e) {
            return notificationDAO.rollback(id, DELETED_OBJECT_NOT_EXISTS, e);
        } catch (WrongEntityIdException e) {
            return notificationDAO.rollback(id, WRONG_DELETED_ID, e);
        } catch (IllegalArgumentException e) {
            return notificationDAO.rollback(id, NULL_ID, e);
        }
        notificationDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertNotification(Notification notification) {
        if (notification == null) return null;
        Integer notifId = null;
        try {
            notifId = notificationDAO.insertNotification(notification);
            LOGGER.info("Get from DB notificationId = " + notifId);
        } catch (IllegalArgumentException e) {
            return notificationDAO.rollback(WRONG_FIELD, e);
        }
        notificationDAO.commit();
        return new IUDAnswer(notifId,true);
    }

    @Override
    public IUDAnswer updateNotification(Integer id, Notification newNotification) {
        if (newNotification == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            newNotification.setObjectId(id);
            Notification oldNotification = notificationDAO.getNotification(id);
            notificationDAO.updateNotification(newNotification, oldNotification);
        } catch (IllegalArgumentException e) {
            return notificationDAO.rollback(WRONG_FIELD, e);
        }
        notificationDAO.commit();
        return new IUDAnswer(id,true);
    }
}
