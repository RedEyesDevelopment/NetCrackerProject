package projectpackage.repository.ordersdao;

import projectpackage.model.orders.Order;
import projectpackage.repository.AbstractDAO;
import projectpackage.repository.daoexceptions.TransactionException;

/**
 * Created by Arizel on 16.05.2017.
 */
public class OrderDAOImpl extends AbstractDAO implements OrderDAO{
    @Override
    public int insertOrder(Order order) throws TransactionException {
        return 0;
    }

    @Override
    public void updateOrder(Order newOrder, Order oldOrder) throws TransactionException {

    }

    @Override
    public int deleteOrder(int id) {
        return deleteSingleEntityById(id);
    }
}
