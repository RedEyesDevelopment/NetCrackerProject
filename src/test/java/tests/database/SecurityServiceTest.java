package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.service.securityservice.SecurityService;

/**
 * Created by Lenovo on 21.05.2017.
 */
public class SecurityServiceTest extends AbstractDatabaseTest {

    @Autowired
    SecurityService securityService;

    @Test
    public void testSecurity(){
        System.out.println(securityService.findLoggedInUsername());
    }
}
