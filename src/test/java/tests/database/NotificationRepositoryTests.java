package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.service.notificationservice.NotificationService;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationRepositoryTests extends AbstractDatabaseTest{

    @Autowired
    NotificationService notificationService;

    @Test
    @Rollback(true)
    public void getAllNotifications() {
        List<Notification> list = notificationService.getAllNotifications("userId", true);
        for (Notification notification:list){
            System.out.println(notification);
        }
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleNotificationById(){
        Notification notification = null;
        int notificationId = 1101;
        notification = notificationService.getSingleNotificationById(notificationId);
        System.out.println(notification);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void deleteNotification(){
        int notificationId = 1102;
        boolean result = notificationService.deleteNotification(notificationId);
        assertTrue(result);
        System.out.println("Delete phone result = " + result);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createNotification(){
        // todo роли и тип надо брать с БД
        Role client = new Role();
        client.setObjectId(1);
        client.setRoleName("client");
        Role admin = new Role();
        admin.setObjectId(2);
        admin.setRoleName("admin");

        User author = new User();
        author.setObjectId(9901);
        author.setRole(client);
        author.setEmail("emailAuthor");
        author.setFirstName("AuthorFirstName");
        author.setLastName("AuthorLastName");

        NotificationType notificationType = new NotificationType();
        notificationType.setOrientedRole(admin);
        notificationType.setNotificationTypeTitle("Some note title");

        Notification notification = new Notification();
        notification.setAuthor(author);
        notification.setMessage("some message");
        notification.setSendDate(new Date(System.currentTimeMillis()));
        notification.setNotificationType(notificationType);
        boolean result = notificationService.insertNotification(notification);
        assertTrue(result);
        System.out.println("Create phone result = " + result);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateNotification(){

    }

    public void getNotificationsBySendDate(){

    }

    public void getNotificationsByExecutedDate(){

    }

    public void getNotificationsByType() {

    }

    public void getNotificationsByAuthor() {

    }

    public void getNotificationsByExecutor() {

    }

    public void getNotificationsForRole() {

    }
}
