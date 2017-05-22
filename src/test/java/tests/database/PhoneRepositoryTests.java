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
import projectpackage.model.support.IUDAnswer;
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
        int phoneId = 2009;
        IUDAnswer iudAnswer = phoneService.deletePhone(phoneId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete phone result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createPhone(){
        Phone phone = new Phone();
        phone.setPhoneNumber("7583475543");
        phone.setUserId(1404);
        IUDAnswer iudAnswer = phoneService.insertPhone(phone);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create phone result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updatePhone(){
        Phone phone = new Phone();
        phone.setUserId(1406);
        phone.setPhoneNumber("0638509180");
        IUDAnswer iudAnswer = phoneService.updatePhone(2009, phone);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update phone result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

}