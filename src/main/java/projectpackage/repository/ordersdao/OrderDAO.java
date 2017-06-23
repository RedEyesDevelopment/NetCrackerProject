package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Order;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;

import java.util.List;

/**
 * Created by Arizel on 16.05.2017.
 */
public interface OrderDAO {
    public Order getOrder(Integer id);
    public List<Order> getAllOrder();
    public Integer insertOrder(Order order) throws TransactionException;
    public Integer updateOrder(Order newOrder, Order oldOrder) throws TransactionException;
    public void deleteOrder(int id) throws ReferenceBreakException, WrongEntityIdException, DeletedObjectNotExistsException;
}
