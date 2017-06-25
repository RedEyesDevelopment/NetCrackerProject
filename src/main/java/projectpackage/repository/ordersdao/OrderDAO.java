package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Order;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface OrderDAO extends Commitable, Rollbackable{
    public Order getOrder(Integer id);
    public List<Order> getAllOrder();
    public Integer insertOrder(Order order);
    public Integer updateOrder(Order newOrder, Order oldOrder);
    public void deleteOrder(Integer id);
}
