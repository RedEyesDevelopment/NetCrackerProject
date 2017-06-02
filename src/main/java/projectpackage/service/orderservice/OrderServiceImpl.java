package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.OrderDTO;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.repository.ordersdao.OrderDAO;
import projectpackage.repository.roomsdao.RoomDAO;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.TransactionException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j
@Service
public class OrderServiceImpl implements OrderService{

    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    RoomDAO roomDAO;

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
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Integer roomNumber = room.getRoomNumber();
        for (Order order : allOrders) {
            if (order.getRoom().getRoomNumber().equals(roomNumber)) {
                answer.add(order);
            }
        }
        return answer;
    }

    @Override
    public List<Order> getOrdersByClient(User user) {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        String email = user.getEmail();
        for (Order order : allOrders) {
            if (order.getClient().getEmail().equals(email)) {
                answer.add(order);
            }
        }
        return answer;
    }

    @Override
    public List<Order> getOrdersByRegistrationDate(Date date) {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        for (Order order : allOrders) {
            if (order.getRegistrationDate().equals(date)) {
                answer.add(order);
            }
        }
        return answer;
    }

    // todo необходимо пояснение что должен делать этот метод
    @Override
    public List<Order> getOrdersBySum(long minSum, long maxSum) {
        return null;
    }

    @Override
    public List<Order> getCurrentOrders() {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (order.getLivingStartDate().getTime() < date.getTime()
                    && order.getLivingFinishDate().getTime() > date.getTime()) {
                answer.add(order);
            }
        }
        return answer;
    }

    @Override
    public List<Order> getPreviousOrders() {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (order.getLivingFinishDate().getTime() < date.getTime()) {
                answer.add(order);
            }
        }
        return answer;
    }

    @Override
    public List<Order> getFutureOrders() {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (order.getLivingStartDate().getTime() > date.getTime()) {
                answer.add(order);
            }
        }
        return answer;
    }

    @Override
    public List<Order> getOrdersInRange(Date startDate, Date finishDate) {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (    !(
                        order.getLivingStartDate().getTime() > finishDate.getTime()
                        ||
                        order.getLivingFinishDate().getTime() < startDate.getTime()
                    )
               ) {
                answer.add(order);
            }
        }
        // todo хорошо потестить правильно ли выборка работает
        return answer;
    }

    @Override
    public List<Order> getOrdersForPayConfirme() {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        for (Order order : allOrders) {
            if (order.getIsPaidFor() && !order.getIsConfirmed()) {
                answer.add(order);
            }
        }
        return answer;
    }

    @Override
    public List<Order> getOrdersConfirmed() {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        for (Order order : allOrders) {
            if (order.getIsConfirmed()) {
                answer.add(order);
            }
        }
        return answer;
    }

    @Override
    public List<Order> getOrdersMustToBePaid() {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        for (Order order : allOrders) {
            if (!order.getIsPaidFor()) {
                answer.add(order);
            }
        }
        return answer;
    }



    @Override
    public IUDAnswer createOrder(User client, int roomTypeId, int numberOfResidents, Date start, Date finish, Category category, long summ) {
        Room room = roomDAO.getFreeRoom(roomTypeId, numberOfResidents, start, finish);
        if (null != room) {
            Order order = new Order();
            order.setRegistrationDate(new Date());
            order.setIsPaidFor(false);
            order.setIsConfirmed(false);
            order.setLivingStartDate(start);
            order.setLivingFinishDate(finish);
            order.setSum(summ);
            order.setComment("");
            order.setLastModificator(client);
            order.setRoom(room);
            order.setClient(client);
            return insertOrder(order);
        } else {
            return new IUDAnswer(false, "emptyRoomNotFound");
        }
    }

    @Override
    public Order createOrderTemplate(User client, OrderDTO dto) {
        Room room = roomDAO.getFreeRoom(dto.getRoomTypeId(), dto.getLivingPersons(), dto.getArrival(), dto.getDeparture());
        Order order = new Order();
        if (null != room) {
            order.setRegistrationDate(new Date());
            order.setIsPaidFor(false);
            order.setIsConfirmed(false);
            order.setLivingStartDate(dto.getArrival());
            order.setLivingFinishDate(dto.getDeparture());
            order.setSum(dto.getLivingCost()+dto.getCategoryCost());
            order.setComment("");
            order.setLastModificator(client);
            order.setRoom(room);
            order.setClient(client);
        }
        return order;
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
            LOGGER.warn("Entity has references on self", e);
            return new IUDAnswer(id,false, e.printReferencesEntities());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn("Entity with that id does not exist!", e);
            return new IUDAnswer(id, "deletedObjectNotExists");
        } catch (WrongEntityIdException e) {
            LOGGER.warn("This id belong another entity class!", e);
            return new IUDAnswer(id, "wrongDeleteId");
        }
        return new IUDAnswer(id, true);
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
        return new IUDAnswer(orderId,true, "orderCreated");
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
