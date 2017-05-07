package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.repository.reacdao.ReactEAVManager;

import java.util.List;

@Log4j
@Transactional
public class ReactEAVTest extends AbstractDatabaseTest {
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    ReactEAVManager manager;

    @Test
    public void queryTestOfUsers(){
        List<User> list = (List<User>) manager.createReactEAV(User.class).getEntityCollection();
        for (User user:list){
            System.out.println(user);
        }
        System.out.println(SEPARATOR);
    }

    @Test
    public void queryTestOfPhones(){
        List<Phone> phones = (List<Phone>) manager.createReactEAV(Phone.class).getEntityCollection();
        for (Phone phone:phones){
            System.out.println(phone);
        }
        System.out.println(SEPARATOR);
    }

    @Test
    public void queryTestOfRoles(){
        List<Role> roles = (List<Role>) manager.createReactEAV(Role.class).getEntityCollection();
        for (Role role:roles){
            System.out.println(role);
        }
        System.out.println(SEPARATOR);
    }
}
