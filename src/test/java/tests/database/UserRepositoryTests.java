package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.User;
import projectpackage.service.UserService;

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
        int userId = 900;
        user = userService.getSingleUserById(userId);
        System.out.println(user);
        System.out.println(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteUser(){
        int userId = 901;
        int deletedRows = userService.deleteUserById(userId);
        System.out.println("DeletedRows="+deletedRows);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createUser(){

    }

    @Test
    @Rollback(true)
    public void updateUser(){

    }

}