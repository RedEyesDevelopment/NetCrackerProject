package tests.database;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.orderservice.OrderService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Arizel on 16.05.2017.
 */
public class OrderRepositoryTests extends AbstractDatabaseTest{
    private static final Logger LOGGER = Logger.getLogger(OrderRepositoryTests.class);

    @Autowired
    OrderService orderService;

    @Test
    @Rollback(true)
    public void crudOrderTest() {
        Room insertRoom = new Room();
        insertRoom.setObjectId(127);
        User insertUser = new User();
        insertUser.setObjectId(901);
        Order insertOrder = new Order();
        insertOrder.setRegistrationDate(new Date());
        insertOrder.setIsPaidFor(false);
        insertOrder.setIsConfirmed(false);
        insertOrder.setLivingStartDate(new Date());
        insertOrder.setLivingFinishDate(new Date());
        insertOrder.setSum(7478L);
        insertOrder.setComment("Comment");
        insertOrder.setRoom(insertRoom);
        insertOrder.setClient(insertUser);
        insertOrder.setLastModificator(insertUser);
        IUDAnswer insertAnswer = orderService.insertOrder(insertOrder);
        assertTrue(insertAnswer.isSuccessful());
        LOGGER.info("Create order result = " + insertAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        int orderId = insertAnswer.getObjectId();
        Order insertedOrder = orderService.getSingleOrderById(orderId);
        insertOrder.setObjectId(orderId);
        assertEquals(insertOrder, insertedOrder);

        Room updateRoom = new Room();
        updateRoom.setObjectId(128);
        User updateUser = new User();
        updateUser.setObjectId(901);
        Order updateOrder = new Order();
        updateOrder.setRegistrationDate(new Date());
        updateOrder.setIsPaidFor(true);
        updateOrder.setIsConfirmed(true);
        updateOrder.setLivingStartDate(new Date());
        updateOrder.setLivingFinishDate(new Date());
        updateOrder.setSum(10000L);
        updateOrder.setComment("new Comment");
        updateOrder.setRoom(updateRoom);
        updateOrder.setClient(updateUser);
        updateOrder.setLastModificator(updateUser);
        IUDAnswer iudAnswer = orderService.updateOrder(orderId, updateOrder);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create order result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Order updatedOrder = orderService.getSingleOrderById(orderId);
        assertEquals(updateOrder, updatedOrder);
        IUDAnswer updateAnswer = orderService.deleteOrder(orderId);
        assertTrue(updateAnswer.isSuccessful());
        LOGGER.info("Delete order result = " + updateAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);

        Order deletedOrder = orderService.getSingleOrderById(orderId);
        assertNull(deletedOrder);
    }

    @Test
    @Rollback
    public void getAllOrdersForAdmin() {
        List<Order> orders = orderService.getAllOrderForAdmin();
        assertNotNull(orders);
        LOGGER.info(orders);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getAllOrders() {
        LOGGER.info("IN TESTS:"+Thread.currentThread());
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            LOGGER.info(order);
        }
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getOrdersByRoom(){
        Order order = orderService.getSingleOrderById(2080);
        LOGGER.info(order);
    }

    @Test
    @Rollback(true)
    public void getOrderForAdmin(){
        Order order = orderService.getOrderForAdmin(300);
        LOGGER.info(order);
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void getSingleOrderById(){
        Order order = orderService.getSingleOrderById(2496);
        LOGGER.info(order);
        LOGGER.info(SEPARATOR);
    }


    @Test
    @Rollback(true)
    public void deleteOrder(){
        int orderId = 2069;
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
        Category category = new Category();
        category.setObjectId(450);
        order.setCategory(category);
        order.setRoom(room);
        order.setClient(user);
        order.setLastModificator(user);
        IUDAnswer iudAnswer = orderService.insertOrder(order);
        LOGGER.info(iudAnswer);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info(iudAnswer.getObjectId());
        LOGGER.info("Create order result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

    @Test
    @Rollback(true)
    public void updateOrder(){// check after merge
        Room room = new Room();
        room.setObjectId(128);
        User user = new User();
        user.setObjectId(901);
        Category category = new Category();
        category.setObjectId(450);
        Order order = new Order();
        order.setRegistrationDate(new Date());
        order.setCategory(category);
        order.setIsPaidFor(true);
        order.setIsConfirmed(true);
        order.setLivingStartDate(new Date());
        order.setLivingFinishDate(new Date());
        order.setSum(10000L);
        order.setComment("new Comment");
        order.setRoom(room);
        order.setClient(user);
        order.setLastModificator(user);
        IUDAnswer iudAnswer = orderService.updateOrder(300, order);
        assertTrue(iudAnswer.isSuccessful());
        LOGGER.info("Create order result = " + iudAnswer.isSuccessful());
        LOGGER.info(SEPARATOR);
    }

//    @Test
//    public void getOrder(){
//        Order orderToUpdate = orderService.getSingleOrderById(2100);
//        Order newOrder = (Order) orderToUpdate.clone();
//        newOrder.setIsPaidFor(true);
//        LOGGER.info("old="+orderToUpdate);
//        LOGGER.info("new="+newOrder);
//        IUDAnswer answer = orderService.updateOrder(newOrder.getObjectId(), newOrder);
//        LOGGER.info("result="+answer);
//    }
}
