package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.repository.daoexceptions.TransactionException;
import projectpackage.repository.maintenancedao.JournalRecordDAO;
import projectpackage.repository.ordersdao.ModificationHistoryDAO;
import projectpackage.repository.ordersdao.OrderDAO;

import java.util.Date;
import java.util.List;

@Log4j
@Service
public class OrderServiceImpl implements OrderService{

    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    ModificationHistoryDAO historyDAO;

    @Autowired
    JournalRecordDAO journalRecordDAO;

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
    public boolean deleteOrder(int id) {
        Order order = orderDAO.getOrder(id);
        for (ModificationHistory history : order.getHistorys()) {
            historyDAO.deleteModificationHistory(history.getObjectId());
        }
        for (JournalRecord record : order.getJournalRecords()) {
            journalRecordDAO.deleteJournalRecord(record.getObjectId());
        }
        int count = orderDAO.deleteOrder(id);
        LOGGER.info("Deleted rows : " + count);
        if (count == 0) return false;
        return true;
    }

    @Override
    public boolean insertOrder(Order order) {
        try {
            int orderId = orderDAO.insertOrder(order);
            LOGGER.info("Get from DB orderId = " + orderId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateOrder(int id, Order newOrder) {
        try {
            newOrder.setObjectId(id);
            Order oldOrder = orderDAO.getOrder(id);
            orderDAO.updateOrder(newOrder, oldOrder);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return false;
        }
        return true;
    }
}
