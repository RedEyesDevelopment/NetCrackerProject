package projectpackage.service.orderservice;

import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.dto.IUDAnswer;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface OrderService {
    public List<Order> getOrdersByRoom(Room room);
    public List<Order> getOrdersByClient(User user);
    public List<Order> getOrdersByRegistrationDate(Date date);
    public List<Order> getOrdersBySum(long minSum, long maxSum);
    public List<Order> getCurrentOrders();//livingStartDate < SYSDATE < livingFinishDate ясно?
    public List<Order> getPreviousOrders();//livingFinishDate < SYSDATE
    public List<Order> getFutureOrders();//livingStartDate > SYSDATE
    public List<Order> getOrdersForPayConfirme();
    public List<Order> getOrdersInRange(Date startDate, Date finishDate);
    public List<Order> getOrdersConfirmed(boolean isConfirmed);
    public List<Order> getOrdersPaidFor(boolean isConfirmed);

    public List<Order> getAllOrders();
    public List<Order> getAllOrders(String orderingParameter, boolean ascend);
    public Order getSingleOrderById(int id);
    public IUDAnswer deleteOrder(int id);
    public IUDAnswer insertOrder(Order order);
    public IUDAnswer updateOrder(int id, Order newOrder);
}
