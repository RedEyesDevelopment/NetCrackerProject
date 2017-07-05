package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Order;
import projectpackage.repository.Commitable;
import projectpackage.repository.Rollbackable;

import java.util.List;

public interface OrderDAO extends Commitable, Rollbackable{
    List<Order> getAllOrderForAdmin();
    List<Order> getSimpleOrderList();
    Order getOrderForAdmin(Integer id);
    Order getOrder(Integer id);
    List<Order> getAllOrder();
    Integer insertOrder(Order order);
    Integer updateOrder(Order newOrder, Order oldOrder);
    void deleteOrder(Integer id);
}
