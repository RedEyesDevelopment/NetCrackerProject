package tests.reacdao;

import org.junit.Test;
import projectpackage.model.auth.User;
import projectpackage.repository.reacdao.ReacDAO;
import tests.AbstractTest;

import java.lang.reflect.InvocationTargetException;

public class ReacDAOTest extends AbstractTest{

    @Test
    public void queryTest(){
        ReacDAO reacDAO = new ReacDAO(User.class);
        try {
            reacDAO.doGet();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
