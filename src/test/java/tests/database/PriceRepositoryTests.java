package tests.database;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.Phone;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class PriceRepositoryTests extends AbstractDatabaseTest{
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
        boolean result = phoneService.deletePhone(phoneId);
        assertTrue(result);
        System.out.println("Delete phone result = " + result);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createPhone(){
        Phone phone = new Phone();
        phone.setPhoneNumber("7583475543");
        phone.setUserId(1404);
        boolean result = phoneService.insertPhone(phone);
        assertTrue(result);
        System.out.println("Create phone result = " + result);
        System.out.println(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updatePhone(){
        Phone phone = new Phone();
        phone.setObjectId(1407);
        phone.setUserId(1406);
        phone.setPhoneNumber("0638509180");
        boolean result = phoneService.updatePhone(phone);
        assertTrue(result);
        System.out.println("Update phone result = " + result);
        System.out.println(SEPARATOR);
    }
}
