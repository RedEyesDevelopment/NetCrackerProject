package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.User;
import projectpackage.repository.DeleteDAO;
import projectpackage.service.UserService;

import java.util.List;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
@Transactional(value = "annotationDrivenTransactionManager")
public class RepositoryTests extends AbstractDatabaseTest {
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    UserService userService;

    @Autowired
    DeleteDAO deleteDAO;

    @Test
    public void getAllUsers() {
        List<User> list = userService.getAllUsers("USERFIN.VALUE");
        for (User user:list){
            System.out.println(user);
        }
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void deleteUser(){
        System.out.println("DELETE USER");
        int fuck = deleteDAO.deleteSingleEntityById(134);
        System.out.println("DeletedRows="+fuck);
        System.out.println(SEPARATOR);
    }

}