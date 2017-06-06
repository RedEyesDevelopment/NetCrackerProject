package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.model.notifications.Notification;
import projectpackage.service.adminservice.InMemoryNotifService;

import java.util.Set;

/**
 * Created by Lenovo on 06.06.2017.
 */
public class InMemoryNotifServiceTests extends AbstractDatabaseTest{

    @Autowired
    InMemoryNotifService inMemoryNotifService;

    @Test
    public void getAllNotifications(){
        String userole = "ADMIN";
        Set<Notification> notfs = inMemoryNotifService.getNotificationsForUserole(userole);
        System.out.println(SEPARATOR);
        System.out.println("FOR ROLE = "+userole);
        for (Notification not:notfs){
            System.out.println(not);
        }
    }
}
