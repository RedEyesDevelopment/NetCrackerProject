package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.authservice.UserService;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
public class UserRepositoryTests extends AbstractDatabaseTest {
    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class);

    @Autowired
    PlatformTransactionManager annotationDrivenTransactionManager;

    @Autowired
    UserService userService;

    @Test
    @Rollback(true)
    public void getAllUsers() {
        List<User> list = userService.getAllUsers();
        for (User user:list){
            LOGGER.info(user);
            assertNotNull(user);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleUserById(){
        User user = null;
        int userId = 900;
        user = userService.getSingleUserById(userId);
        assertNotNull(user);
        LOGGER.info(user);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteUser(){
        int userId = 2042;
        IUDAnswer iudAnswer = userService.deleteUser(userId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete user result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createUser(){
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
        IUDAnswer iudAnswer = userService.insertUser(user);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create user result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateUser(){
        Role newRole = new Role();
        newRole.setObjectId(2);
        newRole.setRoleName("Reception");
        User newUser = new User();
        newUser.setObjectId(2042);
        newUser.setEmail("fsdf@gmail.com");
        newUser.setPassword("4324668");
        newUser.setFirstName("Alexander");
        newUser.setLastName("Merl");
        newUser.setAdditionalInfo("My new INFO");
        newUser.setRole(newRole);
        IUDAnswer iudAnswer = userService.updateUser(newUser.getObjectId(), newUser);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update user result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getUsersByRole(){
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getAllUsersWithParameters(){
        LOGGER.info(SEPARATOR);
    }
}