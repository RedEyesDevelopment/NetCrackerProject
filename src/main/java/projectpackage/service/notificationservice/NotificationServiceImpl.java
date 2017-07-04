package projectpackage.service.notificationservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.notificationsdao.NotificationDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j
@Service
public class NotificationServiceImpl implements NotificationService{

    private static final Logger LOGGER = Logger.getLogger(NotificationServiceImpl.class);

    @Autowired
    NotificationDAO notificationDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = notificationDAO.getAllNotifications();
        if (notifications == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return notifications;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public List<Notification> getAllNotExecutedNotifications() {
        return notificationDAO.getAllNotExecutedNotifications();
    }

    @Transactional(readOnly = true)
    @Override
    public Notification getSingleNotificationById(Integer id) {
        Notification notification = notificationDAO.getNotification(id);
        if (notification == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return notification;
    }

    @Transactional(readOnly = true)
    @Override
    public Notification getNotExecutedNotificationById(Integer id) {
        if (null == id){
            return null;
        }
        return notificationDAO.getNotExecutedNotification(id);
    }

    @Transactional
    @Override
    public IUDAnswer deleteNotification(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        notificationDAO.deleteNotification(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertNotification(Notification notification) {
        if (notification == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        Integer notifId = notificationDAO.insertNotification(notification);

        return new IUDAnswer(notifId,true);
    }

    @Transactional
    @Override
    public IUDAnswer updateNotification(Integer id, Notification newNotification) {
        if (newNotification == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        newNotification.setObjectId(id);
        Notification oldNotification = notificationDAO.getNotExecutedNotification(id);
        notificationDAO.updateNotification(newNotification, oldNotification);

        return new IUDAnswer(id,true);
    }
}
