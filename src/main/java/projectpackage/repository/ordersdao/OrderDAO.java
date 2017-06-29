package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Order;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface OrderDAO extends Commitable, Rollbackable{
    List<Order> getAllOrderForAdmin();
    Order getOrderForAdmin(Integer id);
    Order getOrder(Integer id);
    List<Order> getAllOrder();
    Integer insertOrder(Order order);
    Integer updateOrder(Order newOrder, Order oldOrder);
    void deleteOrder(Integer id);
}
