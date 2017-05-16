package tests.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.orders.Order;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class OrderRepositoryTests extends AbstractDatabaseTest{

    @Autowired
    Order order;

    @Test
    @Rollback(true)
    public void getAllOrders() {

    }

    @Test
    @Rollback(true)
    public void getOrdersByRoom(){

    }

    @Test
    @Rollback(true)
    public void getOrdersByClient(){

    }

    @Test
    @Rollback(true)
    public void getOrdersByRegistrationDate(){

    }

    @Test
    @Rollback(true)
    public void getOrdersBySum(){

    }

    @Test
    @Rollback(true)
    public void getCurrentOrders(){

    }

    @Test
    @Rollback(true)
    public void getPreviousOrders(){

    }

    @Test
    @Rollback(true)
    public void getOrdersForPayConfirme(){

    }

    @Test
    @Rollback(true)
    public void getOrdersInRange(){

    }

    @Test
    @Rollback(true)
    public void getOrdersConfirmed(){

    }

    @Test
    @Rollback(true)
    public void getOrdersPaidFor(){

    }

    @Test
    @Rollback(true)
    public void getFutureOrders(){

    }

    @Test
    @Rollback(true)
    public void getSingleOrderById(){

    }


    @Test
    @Rollback(true)
    public void deleteOrder(){

    }

    @Test
    @Rollback(true)
    public void createOrder(){

    }

    @Test
    @Rollback(true)
    public void updateOrder(){

    }
}
