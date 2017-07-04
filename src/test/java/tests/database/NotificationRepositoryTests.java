package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.orders.Order;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.notificationservice.NotificationService;
import projectpackage.service.notificationservice.NotificationServiceImpl;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void crudNotificationTest() {
        User insertAuthor = new User();
        insertAuthor.setObjectId(900);

        NotificationType insertNotificationType = new NotificationType();
        insertNotificationType.setObjectId(11);

        Order insertOrder = new Order();
        insertOrder.setObjectId(300);

        Notification insertNotification = new Notification();
        insertNotification.setExecutedDate(new Date());
        insertNotification.setExecutedBy(insertAuthor);
        insertNotification.setAuthor(insertAuthor);
        insertNotification.setNotificationType(insertNotificationType);
        insertNotification.setMessage("some message");
        insertNotification.setSendDate(new Date());
        insertNotification.setOrder(insertOrder);
        IUDAnswer insertAnswer = notificationService.insertNotification(insertNotification);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Create notification result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int notId = insertAnswer.getObjectId();
        Notification insertedNotification = notificationService.getSingleNotificationById(notId);
        insertNotification.setObjectId(notId);
        assertEquals(insertNotification, insertedNotification);

        User updateAuthor = new User();
        updateAuthor.setObjectId(901);

        NotificationType notificationType = new NotificationType();
        notificationType.setObjectId(10);

        Order updateOrder = new Order();
        updateOrder.setObjectId(300);

        Notification updateNotification = new Notification();
        updateNotification.setExecutedDate(new Date());
        updateNotification.setExecutedBy(updateAuthor);
        updateNotification.setAuthor(updateAuthor);
        updateNotification.setNotificationType(notificationType);
        updateNotification.setMessage("new some message");
        updateNotification.setSendDate(new Date());
        updateNotification.setOrder(updateOrder);
        IUDAnswer iudAnswer = notificationService.updateNotification(notId, updateNotification);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create notification result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Notification updatedNotification = notificationService.getSingleNotificationById(notId);
        assertEquals(updateNotification, updatedNotification);

        IUDAnswer deleteAnswer = notificationService.deleteNotification(notId);
        assertTrue(deleteAnswer.isSuccessful());
        LOGGER.info("Delete notification result = " + deleteAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Notification deletedNotification = notificationService.getSingleNotificationById(notId);
        assertNull(deletedNotification);
    }

    @Test
    @Rollback(true)
    public void getAllNotifications() {
        List<Notification> list = notificationService.getAllNotifications();
        for (Notification notification:list){
            LOGGER.info(notification);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getAllNotExecutedNotifications() {
        List<Notification> list = notificationService.getAllNotExecutedNotifications();
        for (Notification notification:list){
            LOGGER.info(notification);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getNotExecutedNotification() {
        Notification not = notificationService.getNotExecutedNotificationById(1402);
        LOGGER.info(SEPARATOR);
        LOGGER.info(not);
    }

    @Test
    @Rollback(true)
    public void getSingleNotificationById(){
        Notification notification = null;
        int notificationId = 1400;
        notification = notificationService.getSingleNotificationById(notificationId);
        LOGGER.info(notification);
        LOGGER.info(SEPARATOR);
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
