package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.Phone;
import projectpackage.service.notificationservice.NotificationService;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class NotificationTypeRepositoryTests extends AbstractDatabaseTest{

    @Autowired
    NotificationService notificationService;

    @Test
    @Rollback(true)
    public void getAllNotificationTypes() {

    }

    @Test
    @Rollback(true)
    public void getSingleNotificationTypeById(){

    }

    @Test
    @Rollback(true)
    public void getNotificationTypeByRole(){

    }


    @Test
    @Rollback(true)
    public void deleteNotificationType(){

    }

    @Test
    @Rollback(true)
    public void createNotificationType(){

    }

    @Test
    @Rollback(true)
    public void updateNotificationType(){

    }
}
