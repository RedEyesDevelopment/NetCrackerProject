package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Order;
import projectpackage.repository.daoexceptions.TransactionException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface OrderDAO {
    public Order getOrder(Integer id);
    public List<Order> getAllOrder();
    public int insertOrder(Order order) throws TransactionException;
    public void updateOrder(Order newOrder, Order oldOrder) throws TransactionException;
    public int deleteOrder(int id);
}
