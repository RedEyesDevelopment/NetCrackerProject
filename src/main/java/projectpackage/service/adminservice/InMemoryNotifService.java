package projectpackage.service.adminservice;

import projectpackage.model.notifications.Notification;

import java.util.Set;

/**
 * Created by Lenovo on 06.06.2017.
 */
public interface InMemoryNotifService {
    public Set<Notification> getNotificationsForUserole(String roleName);
    public void insertNewNotification(Notification notif);
    public void removeOldNotification(Notification notif);
}