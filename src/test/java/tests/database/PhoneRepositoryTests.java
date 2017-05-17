package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.Phone;
import projectpackage.service.authservice.PhoneService;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
@Transactional(value = "annotationDrivenTransactionManager")
public class PhoneRepositoryTests extends AbstractDatabaseTest {
    private static final Logger LOGGER = Logger.getLogger(PhoneRepositoryTests.class);

    @Autowired
    PhoneService phoneService;

    @Test
    @Rollback(true)
    public void getAllPhones() {
        List<Phone> list = phoneService.getAllPhones("userId", true);
        for (Phone phone:list){
            LOGGER.info(phone);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSinglePhoneById(){
        Phone phone = null;
        int phoneId = 1101;
        phone = phoneService.getSinglePhoneById(phoneId);
        LOGGER.info(phone);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deletePhone(){
        int phoneId = 1102;
        boolean result = phoneService.deletePhone(phoneId);
        assertTrue(result);
        LOGGER.info("Delete phone result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createPhone(){
        Phone phone = new Phone();
        phone.setPhoneNumber("7583475543");
        phone.setUserId(1404);
        boolean result = phoneService.insertPhone(phone);
        assertTrue(result);
        LOGGER.info("Create phone result = " + result);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updatePhone(){
        Phone phone = new Phone();
        phone.setObjectId(1407);
        phone.setUserId(1406);
        phone.setPhoneNumber("0638509180");
        boolean result = phoneService.updatePhone(1407, phone);
        assertTrue(result);
        LOGGER.info("Update phone result = " + result);
        LOGGER.info(SEPARATOR);
    }

}