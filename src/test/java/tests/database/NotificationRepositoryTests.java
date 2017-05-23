package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.orders.Order;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.notificationservice.NotificationService;
import projectpackage.service.notificationservice.NotificationServiceImpl;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationRepositoryTests extends AbstractDatabaseTest{

    private static final Logger LOGGER = Logger.getLogger(NotificationServiceImpl.class);

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
        int notificationId = 1400;
        notification = notificationService.getSingleNotificationById(notificationId);
        System.out.println(notification);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void deleteNotification(){
        int notId = 2065;
        IUDAnswer iudAnswer = notificationService.deleteNotification(notId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete notification result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createNotification(){
        User author = new User();
        author.setObjectId(900);

        NotificationType notificationType = new NotificationType();
        notificationType.setObjectId(11);

        Order order = new Order();
        order.setObjectId(300);

        Notification notification = new Notification();
        notification.setExecutedDate(new Date());
        notification.setExecutedBy(author);
        notification.setAuthor(author);
        notification.setNotificationType(notificationType);
        notification.setMessage("some message");
        notification.setSendDate(new Date());
        notification.setOrder(order);
        IUDAnswer iudAnswer = notificationService.insertNotification(notification);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create notification result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateNotification(){//need check after merge
        User author = new User();
        author.setObjectId(901);

        NotificationType notificationType = new NotificationType();
        notificationType.setObjectId(10);

        Order order = new Order();
        order.setObjectId(300);

        Notification notification = new Notification();
        notification.setObjectId(2065);
        notification.setExecutedDate(new Date());
        notification.setExecutedBy(author);
        notification.setAuthor(author);
        notification.setNotificationType(notificationType);
        notification.setMessage("new some message");
        notification.setSendDate(new Date());
        notification.setOrder(order);
        IUDAnswer iudAnswer = notificationService.updateNotification( 2065, notification);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create notification result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
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
