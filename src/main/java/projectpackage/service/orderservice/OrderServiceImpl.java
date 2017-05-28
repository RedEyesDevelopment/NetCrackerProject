package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.model.support.IUDAnswer;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.ordersdao.OrderDAO;

import java.util.ArrayList;
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
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Integer roomNumber = room.getRoomNumber();
        for (Order order : allOrders) {
            if (order.getRoom().getRoomNumber().equals(roomNumber)) {
                anwer.add(order);
            }
        }
        return anwer;
    }

    @Override
    public List<Order> getOrdersByClient(User user) {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        String email = user.getEmail();
        for (Order order : allOrders) {
            if (order.getClient().getEmail().equals(email)) {
                anwer.add(order);
            }
        }
        return anwer;
    }

    @Override
    public List<Order> getOrdersByRegistrationDate(Date date) {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        for (Order order : allOrders) {
            if (order.getRegistrationDate().equals(date)) {
                anwer.add(order);
            }
        }
        return anwer;
    }

    // todo необходимо пояснение что должен делать этот метод
    @Override
    public List<Order> getOrdersBySum(long minSum, long maxSum) {
        return null;
    }

    @Override
    public List<Order> getCurrentOrders() {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (order.getLivingStartDate().getTime() < date.getTime()
                    && order.getLivingFinishDate().getTime() > date.getTime()) {
                anwer.add(order);
            }
        }
        return anwer;
    }

    @Override
    public List<Order> getPreviousOrders() {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (order.getLivingFinishDate().getTime() < date.getTime()) {
                anwer.add(order);
            }
        }
        return anwer;
    }

    @Override
    public List<Order> getFutureOrders() {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (order.getLivingStartDate().getTime() > date.getTime()) {
                anwer.add(order);
            }
        }
        return anwer;
    }

    @Override
    public List<Order> getOrdersForPayConfirme() {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        for (Order order : allOrders) {
            if (order.getIsPaidFor() && !order.getIsConfirmed()) {
                anwer.add(order);
            }
        }
        return anwer;
    }

    @Override
    public List<Order> getOrdersInRange(Date startDate, Date finishDate) {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (    (startDate.getTime() > order.getLivingStartDate().getTime()
                    && startDate.getTime() < order.getLivingFinishDate().getTime())
                ||
                    (finishDate.getTime() > order.getLivingStartDate().getTime()
                    && finishDate.getTime() < order.getLivingFinishDate().getTime())
                ||
                    (startDate.getTime() < order.getLivingStartDate().getTime()
                    && finishDate.getTime() > order.getLivingFinishDate().getTime())
                ||
                    (startDate.getTime() > order.getLivingStartDate().getTime()
                    && finishDate.getTime() < order.getLivingFinishDate().getTime())
                ) {
                anwer.add(order);
            }
        }
        // todo хорошо потестить правильно ли выборка работает
        return anwer;
    }

    @Override
    public List<Order> getOrdersConfirmed(boolean isConfirmed) {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        for (Order order : allOrders) {
            if (order.getIsConfirmed()) {
                anwer.add(order);
            }
        }
        return anwer;
    }

    @Override
    public List<Order> getOrdersPaidFor(boolean isConfirmed) {
        List<Order> anwer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        for (Order order : allOrders) {
            if (order.getIsPaidFor()) {
                anwer.add(order);
            }
        }
        return anwer;
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
            return new IUDAnswer(false, e.printReferencesEntities());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer insertOrder(Order order) {
        try {
            int orderId = orderDAO.insertOrder(order);
            LOGGER.info("Get from DB orderId = " + orderId);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }

    @Override
    public IUDAnswer updateOrder(int id, Order newOrder) {
        try {
            newOrder.setObjectId(id);
            Order oldOrder = orderDAO.getOrder(id);
            orderDAO.updateOrder(newOrder, oldOrder);
        } catch (TransactionException e) {
            LOGGER.warn("Catched transactionException!!!", e);
            return new IUDAnswer(false, e.getMessage());
        }
        return new IUDAnswer(true);
    }
}
