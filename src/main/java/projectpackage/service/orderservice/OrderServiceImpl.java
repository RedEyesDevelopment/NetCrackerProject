package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.dto.IUDAnswer;
import projectpackage.repository.daoexceptions.ReferenceBreakException;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.ordersdao.OrderDAO;

import java.util.Date;
import java.util.List;

@Log4j
@Service
public class OrderServiceImpl implements OrderService{

    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    OrderDAO orderDAO;

    @Override
    public List<Order> getAllOrders(String orderingParameter, boolean ascend) {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderDAO.getAllOrder();
        if (orders == null) LOGGER.info("Returned NULL!!!");
        return orders;
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
        Order order = orderDAO.getOrder(id);
        if (order == null) LOGGER.info("Returned NULL!!!");
        return order;
    }

    @Override
    public IUDAnswer deleteOrder(int id) {
        try {
            orderDAO.deleteOrder(id);
        } catch (ReferenceBreakException e) {
            return new IUDAnswer(id,false, e.printReferencesEntities());
        }
        return new IUDAnswer(id,true);
    }

    @Override
    public IUDAnswer insertOrder(Order order) {
        Integer orderId = null;
        try {
            orderId = orderDAO.insertOrder(order);
            LOGGER.info("Get from DB orderId = " + orderId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(orderId,false, "transactionInterrupt");
        }
        return new IUDAnswer(orderId,true);
    }

    @Override
    public IUDAnswer updateOrder(int id, Order newOrder) {
        try {
            newOrder.setObjectId(id);
            Order oldOrder = orderDAO.getOrder(id);
            orderDAO.updateOrder(newOrder, oldOrder);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(id,false, "transactionInterrupt");
        }
        return new IUDAnswer(id,true);
    }
}
