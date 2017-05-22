package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.Role;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.notificationservice.NotificationTypeService;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationTypeRepositoryTests extends AbstractDatabaseTest {

    private static final Logger LOGGER = Logger.getLogger(NotificationTypeRepositoryTests.class);

    @Autowired
    NotificationTypeService notificationTypeService;

    @Test
    @Rollback(true)
    public void getAllNotificationTypes() {
        List<NotificationType> list = notificationTypeService.getAllNotificationTypes("notif_type_title", true);
        for (NotificationType notificationType : list) {
            LOGGER.info(notificationType);
            assertNotNull(notificationType);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleNotificationTypeById() {
        NotificationType notificationType = null;
        int notifTypeId = 2067;
        notificationType = notificationTypeService.getSingleNotificationTypeById(notifTypeId);
        assertNotNull(notificationType);
        LOGGER.info(notificationType);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getNotificationTypeByRole() {

    }


    @Test
    @Rollback(true)
    public void deleteNotificationType() {
        int notifTypeId = 2067;
        IUDAnswer iudAnswer = notificationTypeService.deleteNotificationType(notifTypeId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete notifType result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createNotificationType() {
        Role role = new Role();
        role.setRoleName("Admin");
        role.setObjectId(1);
        NotificationType notificationType = new NotificationType();
        notificationType.setNotificationTypeTitle("TestNotificationType");
        notificationType.setOrientedRole(role);
        IUDAnswer iudAnswer = notificationTypeService.insertNotificationType(notificationType);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create notification result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateNotificationType() {
        Role newRole = new Role();
        newRole.setObjectId(2);
        newRole.setRoleName("Reception");
        NotificationType notificationType = new NotificationType();
        notificationType.setObjectId(2067);
        notificationType.setOrientedRole(newRole);
        notificationType.setNotificationTypeTitle("UpdateNotifTypeTEST");
        IUDAnswer iudAnswer = notificationTypeService.updateNotificationType(notificationType.getObjectId(), notificationType);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update notification result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
