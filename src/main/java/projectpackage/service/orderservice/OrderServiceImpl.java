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
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.support.ServiceUtils;

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

    @Autowired
    ServiceUtils serviceUtils;

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

    @Override
    public List<Order> getCurrentOrders() {
        List<Order> answer = new ArrayList<>();
        List<Order> allOrders = getAllOrders();
        Date date = new Date();
        for (Order order : allOrders) {
            if (order.getLivingStartDate().getTime() <= date.getTime()
                    && order.getLivingFinishDate().getTime() >= date.getTime()) {
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
        return answer;
    }

    @Override
    public List<Order> getOrdersForPayConfirmed() {
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
        if (start == null || finish == null || category == null) return new IUDAnswer(false, WRONG_DATES + WRONG_FIELD);
        boolean isValidDates = serviceUtils.checkDates(start, finish);
        if (!isValidDates) return new IUDAnswer(false, WRONG_DATES);
        Room room = roomDAO.getFreeRoom(roomTypeId, numberOfResidents, start, finish);
        if (null != room) {
            Order order = new Order();
            order.setRegistrationDate(new Date());
            order.setIsPaidFor(false);
            order.setIsConfirmed(false);
            order.setLivingStartDate(start);
            order.setLivingFinishDate(finish);
            order.setSum(summ);
            order.setComment("Nothing added!");
            order.setLastModificator(client);
            order.setRoom(room);
            order.setClient(client);
            return insertOrder(order);
        } else {
            return new IUDAnswer(false, EMPTY_ROOM_NOT_FOUND);
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
    public Order getSingleOrderById(Integer id) {
        Order order = orderDAO.getOrder(id);
        if (order == null) LOGGER.info("Returned NULL!!!");
        return order;
    }

    @Override
    public IUDAnswer deleteOrder(Integer id) {
        if (id == null) return new IUDAnswer(false, NULL_ID);
        try {
            orderDAO.deleteOrder(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage());
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage());
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new IUDAnswer(id, false, NULL_ID, e.getMessage());
        }
        orderDAO.commit();
        return new IUDAnswer(id, true);
    }

    @Override
    public IUDAnswer insertOrder(Order order) {
        if (order == null) return null;
        boolean isValidDates = serviceUtils.checkDates(order.getLivingStartDate(), order.getLivingFinishDate());
        if (!isValidDates) return new IUDAnswer(false, WRONG_DATES);
        Integer orderId = null;
        try {
            orderId = orderDAO.insertOrder(order);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(false, WRONG_FIELD, e.getMessage());
        }
        orderDAO.commit();
        return new IUDAnswer(orderId,true, ORDER_CREATED);
    }

    @Override
    public IUDAnswer updateOrder(Integer id, Order newOrder) {
        if (newOrder == null) return null;
        if (id == null) return new IUDAnswer(false, NULL_ID);
        boolean isValidDates = serviceUtils.checkDates(newOrder.getLivingStartDate(), newOrder.getLivingFinishDate());
        if (!isValidDates) return new IUDAnswer(false, WRONG_DATES);
        try {
            newOrder.setObjectId(id);
            Order oldOrder = orderDAO.getOrder(id);
            orderDAO.updateOrder(newOrder, oldOrder);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new IUDAnswer(id,false, WRONG_FIELD, e.getMessage());
        }
        orderDAO.commit();
        return new IUDAnswer(id,true);
    }
}
