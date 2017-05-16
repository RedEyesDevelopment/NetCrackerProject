package tests.database;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.model.auth.Phone;
import projectpackage.service.authservice.PhoneService;

import java.util.List;

/**
 * Created by Gvozd on 06.01.2017.
 */
@Log4j
@Transactional(value = "annotationDrivenTransactionManager")
public class PhoneRepositoryTests extends AbstractDatabaseTest {
    private final String SEPARATOR = "**********************************************************";

    @Autowired
    PhoneService phoneService;

    @Test
    @Rollback(true)
    public void getAllPhones() {
        List<Phone> list = phoneService.getAllPhones("userId", true);
        for (Phone phone:list){
            System.out.println(phone);
        }
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSinglePhoneById(){
        Phone phone = null;
        int phoneId = 1101;
        phone = phoneService.getSinglePhoneById(phoneId);
        System.out.println(phone);
        System.out.println(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deletePhone(){
        int phoneId = 1102;
        int deletedRows = phoneService.deletePhone(phoneId);
        System.out.println("DeletedRows="+deletedRows);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createPhone(){
        Phone phone = new Phone();
        phone.setObjectId(1407);
        phone.setPhoneNumber("7583475543");
        phone.setUserId(1404);
        boolean result = phoneService.insertPhone(phone);
        System.out.println(result);
    }

    @Test
    @Rollback(true)
    public void updatePhone(){
        Phone phone = new Phone();
        phone.setObjectId(1407);
        phone.setUserId(1406);
        phone.setPhoneNumber("0638509180");
        boolean result = phoneService.updatePhone(phone);
        System.out.println(result);
    }

}