package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.service.UserService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
@Transactional(value = "annotationDrivenTransactionManager")
public class UserRepositoryTests extends AbstractDatabaseTest {
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    UserService userService;

    @Test
    @Rollback(true)
    public void getAllUsers() {
        List<User> list = userService.getAllUsers("email", true);
        for (User user:list){
            System.out.println(user);
        }
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleUserById(){
        User user = null;
        int userId = 1402;
        user = userService.getSingleUserById(userId);
        System.out.println(user);
        System.out.println(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteUser(){
        int userId = 1402;
        int deletedRows = userService.deleteUserById(userId);
        System.out.println("DeletedRows="+deletedRows);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createUser(){
        //TODO пока предполагается, что null и empty вообще не будет, потом надо переделать все тесты так чтобы инсерт падал!
        Role role = new Role();
        role.setRoleName("Admin");
        role.setObjectId(1);
        User user = new User();
        user.setObjectId(1406);
        user.setEmail("random@mail.ru");
        user.setPassword("4324325fa");
        user.setFirstName("Alex");
        user.setLastName("Merlyan");
        user.setAdditionalInfo("nothing");
        user.setRole(role);
        boolean result = userService.insertUser(user);
        System.out.println(result);
    }

    @Test
    @Rollback(true)
    public void updateUser(){
        //TODO пока предполагается, что null и empty вообще не будет, потом надо переделать все тесты так чтобы апдейт падал!
        Role newRole = new Role();
        newRole.setObjectId(2);
        newRole.setRoleName("Reception");
        User newUser = new User();
        newUser.setObjectId(1404);
        newUser.setEmail("fsdf@gmail.com");
        newUser.setPassword("4324668");
        newUser.setFirstName("Alexander");
        newUser.setLastName("Merl");
        newUser.setAdditionalInfo("My new INFO");
        newUser.setRole(newRole);
        boolean result = userService.updateUser(newUser);
        System.out.println(result);
    }

}