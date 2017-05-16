package projectpackage.service.orderservice;

import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;

import java.util.Date;
import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public class OrderServiceImpl implements OrderService{
    @Override
    public List<Order> getAllOrders(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    @Override
    public List<Order> getOrdersByRoom(Room room) {
        return null;
    }

    @Override
    public List<Order> getOrdersByClient(User user) {
        return null;
    }

    @Override
    public List<Order> getOrdersByRegistrationDate(Date date) {
        return null;
    }

    @Override
    public List<Order> getOrdersBySum(long minSum, long maxSum) {
        return null;
    }

    @Override
    public List<Order> getCurrentOrders() {
        return null;
    }

    @Override
    public List<Order> getPreviousOrders() {
        return null;
    }

    @Override
    public List<Order> getFutureOrders() {
        return null;
    }

    @Override
    public List<Order> getOrdersForPayConfirme() {
        return null;
    }

    @Override
    public List<Order> getOrdersInRange(Date startDate, Date finishDate) {
        return null;
    }

    @Override
    public List<Order> getOrdersConfirmed(boolean isConfirmed) {
        return null;
    }

    @Override
    public List<Order> getOrdersPaidFor(boolean isConfirmed) {
        return null;
    }

    @Override
    public Order getSingleOrderById(int id) {
        return null;
    }

    @Override
    public boolean deleteOrder(Order order) {
        return false;
    }

    @Override
    public boolean insertOrder(Order order) {
        return false;
    }

    @Override
    public boolean updateOrder(Order newOrder) {
        return false;
    }
}
