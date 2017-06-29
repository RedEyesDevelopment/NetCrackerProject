package projectpackage.service.adminservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.notifications.Notification;
import projectpackage.service.notificationservice.NotificationService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by Lenovo on 06.06.2017.
 */
@Service
public class InMemoryNotifServiceImpl implements InMemoryNotifService{

    private static final String ADMINROLETYPE = "ADMIN";
    private static final String RECEPTIONROLETYPE = "RECEPTION";

    @Autowired
    NotificationService notificationService;

    private Set<Notification> receptionNotifications;
    private Set<Notification> adminNotifications;

    @PostConstruct
    public void init() {
        receptionNotifications = new HashSet<>();
        adminNotifications = new HashSet<>();
        List<Notification> notifications = notificationService.getAllNotificationsForInMemoryService();
        for (Notification notification:notifications){
            if (notification.getExecutedDate()==null){
                if (notification.getNotificationType().getOrientedRole().getRoleName().equals(ADMINROLETYPE)){
                    adminNotifications.add(notification);
                } else {
                    receptionNotifications.add(notification);
                }
            }
        }
    }

    @Override
    public Set<Notification> getNotificationsForUserole(String roleName) {
        if (null!=roleName){
            if (roleName.equals(ADMINROLETYPE)){
                return adminNotifications;
            } else if (roleName.equals(RECEPTIONROLETYPE)){
                return receptionNotifications;
            }
        }
        return null;
    }

    @Override
    public void insertNewNotification(Notification notif) {
//        if (notif.getNotificationType().getOrientedRole().getRoleName().equals(ADMINROLETYPE)){
//            adminNotifications.add(notif);
//        } else if (notif.getNotificationType().getOrientedRole().getRoleName().equals(RECEPTIONROLETYPE)){
//            receptionNotifications.add(notif);
//        }
    }

    @Override
    public void removeOldNotification(Notification notif) {
        int targetId = notif.getObjectId();
        Predicate<Notification> notificationPredicate = (s)-> s.getObjectId()==targetId;
        if (notif.getNotificationType().getOrientedRole().getRoleName().equals(ADMINROLETYPE)){
            adminNotifications.removeIf(notificationPredicate);
        } else if (notif.getNotificationType().getOrientedRole().getRoleName().equals(RECEPTIONROLETYPE)){
            adminNotifications.removeIf(notificationPredicate);
        }
    }
}
