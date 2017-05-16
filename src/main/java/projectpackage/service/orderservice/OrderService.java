package projectpackage.service.orderservice;

import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface OrderService {
    public List<Order> getOrdersByRoom(Room room);//TODO Denis
    public List<Order> getOrdersByClient(User user);//TODO Denis
    public List<Order> getOrdersByRegistrationDate(Date date);//TODO Denis
    public List<Order> getOrdersBySum(long minSum, long maxSum);//TODO Denis
    public List<Order> getCurrentOrders();//livingStartDate < SYSDATE < livingFinishDate ясно?//TODO Denis
    public List<Order> getPreviousOrders();//livingFinishDate < SYSDATE//TODO Denis
    public List<Order> getFutureOrders();//SYSDATE < livingStartDate//TODO Denis
    public List<Order> getOrdersForPayConfirme();//TODO Denis
    public List<Order> getOrdersInRange(Date startDate, Date finishDate);//TODO Denis
    public List<Order> getOrdersConfirmed(boolean isConfirmed);//TODO Denis
    public List<Order> getOrdersPaidFor(boolean isConfirmed);//TODO Denis

    public List<Order> getAllOrders();//TODO Pacanu
    public List<Order> getAllOrders(String orderingParameter, boolean ascend);//TODO Pacanu
    public Order getSingleOrderById(int id);//TODO Pacanu
    public boolean deleteOrder(int id);//TODO Pacanu
    public boolean insertOrder(Order order);//TODO Pacanu
    public boolean updateOrder(Order newOrder);//TODO Pacanu
}
