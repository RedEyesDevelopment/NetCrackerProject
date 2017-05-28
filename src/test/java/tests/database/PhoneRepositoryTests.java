package tests.database;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.Phone;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.authservice.PhoneService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void crudPhoneTest() {
        Phone insertPhone = new Phone();
        insertPhone.setPhoneNumber("0638509108");
        insertPhone.setUserId(900);
        IUDAnswer insertAnswer = phoneService.insertPhone(insertPhone);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Create phone result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int phoneId = insertAnswer.getObjectId();
        Phone insertedPhone = phoneService.getSinglePhoneById(phoneId);
        insertPhone.setObjectId(phoneId);
        assertEquals(insertPhone, insertedPhone);

        Phone updatePhone = new Phone();
        updatePhone.setUserId(900);
        updatePhone.setPhoneNumber("0638509180");
        IUDAnswer updateAnswer = phoneService.updatePhone(phoneId, updatePhone);
        assertTrue(updateAnswer.isSuccessful());
        LOGGER.info("Update phone result = " + updateAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Phone updatedPhone = phoneService.getSinglePhoneById(phoneId);
        assertEquals(updatePhone, updatedPhone);

        IUDAnswer deleteAnswer = phoneService.deletePhone(phoneId);
        assertTrue(deleteAnswer.isSuccessful());
        LOGGER.info("Delete phone result = " + deleteAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Phone deletedPhone = phoneService.getSinglePhoneById(phoneId);
        assertNull(deletedPhone);
    }

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
        int phoneId = 2070;
        IUDAnswer iudAnswer = phoneService.deletePhone(phoneId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete phone result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createPhone(){
        Phone phone = new Phone();
        phone.setPhoneNumber("0638509108");
        phone.setUserId(901);
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
        IUDAnswer iudAnswer = phoneService.updatePhone(2070, phone);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Update phone result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

}