package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.service.authservice.UserService;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
public class UserRepositoryTests extends AbstractDatabaseTest {
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    PlatformTransactionManager annotationDrivenTransactionManager;

    @Autowired
    UserService userService;

    @Test
    @Rollback(true)
    public void getAllUsers() {
        List<User> list = userService.getAllUsers("email", true);
        for (User user:list){
            log.info(user);
            assertNotNull(user);
        }
        log.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleUserById(){
        User user = null;
        int userId = 900;
        user = userService.getSingleUserById(userId);
        assertNotNull(user);
        log.info(user);
        log.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteUser(){
        int userId = 2006;
        boolean result = userService.deleteUser(userId);
        assertTrue(result);
        log.info("Delete user result = " + result);
        log.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createUser(){
        //TODO пока предполагается, что null и empty вообще не будет, потом надо переделать все тесты так чтобы инсерт падал!
        Role role = new Role();
        role.setRoleName("Admin");
        role.setObjectId(1);
        User user = new User();
        user.setEmail("random@mail.ru");
        user.setPassword("4324325fa");
        user.setFirstName("Alex");
        user.setLastName("Merlyan");
        user.setAdditionalInfo("nothing");
        user.setRole(role);
        boolean result = userService.insertUser(user);
        assertTrue(result);
        log.info("Create user result = " + result);
        log.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateUser(){
        //TODO пока предполагается, что null и empty вообще не будет, потом надо переделать все тесты так чтобы апдейт падал!
        Role newRole = new Role();
        newRole.setObjectId(2);
        newRole.setRoleName("Reception");
        User newUser = new User();
        newUser.setObjectId(2003);
        newUser.setEmail("fsdf@gmail.com");
        newUser.setPassword("4324668");
        newUser.setFirstName("Alexander");
        newUser.setLastName("Merl");
        newUser.setAdditionalInfo("My new INFO");
        newUser.setRole(newRole);
        boolean result = userService.updateUser(newUser.getObjectId(), newUser);
        assertTrue(result);
        log.info("Update user result = " + result);
        log.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getUsersByRole(){
        log.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getAllUsersWithParameters(){
        log.info(SEPARATOR);
    }
}