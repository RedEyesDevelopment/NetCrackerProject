package tests.database;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.orderservice.OrderService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Arizel on 16.05.2017.
 */
public class OrderRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(OrderRepositoryTests.class);

    @Autowired
    OrderService orderService;

    @Test
    @Rollback(true)
    public void getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            LOGGER.info(order);
        }
        LOGGER.info(SEPARATOR);
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
        Order order = orderService.getSingleOrderById(534);
        LOGGER.info(order);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteOrder(){
        int orderId = 2031;
        IUDAnswer iudAnswer = orderService.deleteOrder(orderId);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Delete order result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void createOrder(){
        Room room = new Room();
        room.setObjectId(127);
        User user = new User();
        user.setObjectId(900);
        Order order = new Order();
        order.setRegistrationDate(new Date());
        order.setIsPaidFor(false);
        order.setIsConfirmed(false);
        order.setLivingStartDate(new Date());
        order.setLivingFinishDate(new Date());
        order.setSum(7478L);
        order.setComment("Comment");
        order.setRoom(room);
        order.setClient(user);
        IUDAnswer iudAnswer = orderService.insertOrder(order);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create order result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateOrder(){
        Room room = new Room();
        room.setObjectId(128);
        User user = new User();
        user.setObjectId(901);
        Order order = new Order();
        order.setRegistrationDate(new Date());
        order.setIsPaidFor(true);
        order.setIsConfirmed(true);
        order.setLivingStartDate(new Date());
        order.setLivingFinishDate(new Date());
        order.setSum(10000L);
        order.setComment("new Comment");
        order.setRoom(room);
        order.setClient(user);
        IUDAnswer iudAnswer = orderService.updateOrder(2031, order);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create order result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }
}
