package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Order;
import projectpackage.repository.reacdao.exceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface OrderDAO {
    public void insertOrder(Order order) throws TransactionException;
    public void updateOrder(Order newOrder, Order oldOrder) throws TransactionException;
}
