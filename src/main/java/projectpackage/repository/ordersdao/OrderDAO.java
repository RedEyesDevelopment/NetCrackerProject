package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Order;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface OrderDAO {
    public int insertOrder(Order order) throws TransactionException;
    public void updateOrder(Order newOrder, Order oldOrder) throws TransactionException;
    public int deleteOrder(int id);
}