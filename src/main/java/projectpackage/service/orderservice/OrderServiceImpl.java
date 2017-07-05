package projectpackage.service.orderservice;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.dto.ChangeOrderDTO;
import projectpackage.dto.FreeRoomsUpdateOrderDTO;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.OrderDTO;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.ordersdao.OrderDAO;
import projectpackage.service.roomservice.RoomService;
import projectpackage.service.roomservice.RoomTypeService;
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
    RoomService roomService;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    ServiceUtils serviceUtils;

    @Autowired
    CategoryService categoryService;

    @Transactional(readOnly = true)
    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderDAO.getAllOrder();
        if (orders == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return orders;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> getSimpleOrderList() {
        return orderDAO.getSimpleOrderList();
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
        if (start == null || finish == null || category == null) {
            return new IUDAnswer(false, WRONG_DATES + WRONG_FIELD);
        }
        boolean isValidDates = serviceUtils.checkDates(start, finish);
        if (!isValidDates) {
            return new IUDAnswer(false, WRONG_DATES);
        }
        Room room = roomService.getFreeRoom(roomTypeId, numberOfResidents, start, finish);
        if (null != room) {
            Order order = new Order();
            order.setRegistrationDate(new Date());
            order.setIsPaidFor(Boolean.FALSE);
            order.setIsConfirmed(Boolean.FALSE);
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

    @Transactional(readOnly = true)
    @Override
    public List<Order> getAllOrderForAdmin() {
        List<Order> orders = orderDAO.getAllOrderForAdmin();
        if (orders == null) {
            LOGGER.info("Returned NULL!!!");
        }
        for (Order order : orders) {
            order.getClient().setPassword(null);
            order.getLastModificator().setPassword(null);
        }
        return orders;
    }

    @Transactional(readOnly = true)
    @Override
    public Order getOrderForAdmin(Integer id) {
        Order order = orderDAO.getOrderForAdmin(id);
        if (order == null) {
            LOGGER.info("Returned NULL!!!");
        }
        order.getClient().setPassword(null);
        order.getLastModificator().setPassword(null);
        return order;
    }

    @Override
    public Order createOrderTemplate(User client, User lastModificator, OrderDTO dto) {
        Room room = roomService.getFreeRoom(dto.getRoomTypeId(), dto.getLivingPersons(), dto.getArrival(), dto.getDeparture());
        Order order = new Order();
        if (null != room) {
            order.setRegistrationDate(new Date());
            order.setIsPaidFor(Boolean.FALSE);
            order.setIsConfirmed(Boolean.FALSE);
            order.setLivingStartDate(dto.getArrival());
            order.setLivingFinishDate(dto.getDeparture());
            order.setLastModificator(lastModificator);
            order.setSum(dto.getLivingCost()+dto.getCategoryCost());
            order.setComment("");
            order.setRoom(room);
            order.setClient(client);
        }
        return order;
    }

    @Override
    public FreeRoomsUpdateOrderDTO getFreeRoomsToUpdateOrder(Integer orderId, ChangeOrderDTO changeOrderDTO) {
        boolean isValidDates = serviceUtils.checkDatesForUpdate(changeOrderDTO.getLivingStartDate(), changeOrderDTO.getLivingFinishDate());
        if (!isValidDates) {
            return new FreeRoomsUpdateOrderDTO();
        }
        List<Room> freeRooms = roomService.getFreeRooms(changeOrderDTO.getRoomTypeId(), changeOrderDTO.getNumberOfResidents(),
                changeOrderDTO.getLivingStartDate(), changeOrderDTO.getLivingFinishDate());
        Order currentOrder = getSingleOrderById(orderId);
        if ((currentOrder.getLivingFinishDate().getTime() <= (changeOrderDTO.getLivingFinishDate().getTime()))
                && (currentOrder.getRoom().getRoomType().getObjectId() == changeOrderDTO.getRoomTypeId())) {
            List<Room> freeRoomsOnNextDays = roomService.getFreeRooms(changeOrderDTO.getRoomTypeId(), changeOrderDTO.getNumberOfResidents(),
                    new Date(currentOrder.getLivingFinishDate().getTime() + 10_000L), changeOrderDTO.getLivingFinishDate());
            int currentRoomId = currentOrder.getRoom().getObjectId();
            boolean isFree = false;
            for (Room room : freeRoomsOnNextDays) {
                if (room.getObjectId() == currentRoomId) {
                    isFree = true;
                    break;
                }
            }
            if (isFree) {
                freeRooms.add(currentOrder.getRoom());
            }
        }

        FreeRoomsUpdateOrderDTO dto = new FreeRoomsUpdateOrderDTO();

        if (freeRooms.isEmpty()) {
            return dto;
        }

        RoomType roomType = new RoomType();
        roomType.setObjectId(changeOrderDTO.getRoomTypeId());
        Long livingCost = roomTypeService.getLivingCost(changeOrderDTO.getLivingStartDate(),
                changeOrderDTO.getLivingFinishDate(), changeOrderDTO.getNumberOfResidents(), roomType);
        Category category = new Category();
        category.getCategoryPrice();
        Long categoryPrice = categoryService.getSingleCategoryById(changeOrderDTO.getCategoryId()).getCategoryPrice();
        Long days = (changeOrderDTO.getLivingFinishDate().getTime() - changeOrderDTO.getLivingStartDate().getTime()) / (24 * 60 * 60 * 1000);
        Long categoryCost = categoryPrice * days;
        dto.setCategoryCost(categoryCost);
        dto.setLivingCost(livingCost);
        dto.setTotal(categoryCost + livingCost);
        dto.setRooms(freeRooms);
        return dto;
    }

    @Override
    public IUDAnswer setNewDataIntoOrder(Integer orderId, Integer lastModificatorId, ChangeOrderDTO changeOrderDTO, OrderDTO orderDTO) {
        FreeRoomsUpdateOrderDTO dto = getFreeRoomsToUpdateOrder(orderId, changeOrderDTO);
        boolean isExistsInRooms = false;
        for (Room room : dto.getRooms()) {
            if (room.getObjectId() == orderDTO.getRoomId()) {
                isExistsInRooms = true;
                break;
            }
        }
        if (!isExistsInRooms) {
            return new IUDAnswer(false, ROOM_NOT_AVAILABLE);
        }

        User lastModificator = new User();
        lastModificator.setObjectId(lastModificatorId);
        Order order = orderDAO.getOrder(orderId);
        order.getCategory().setObjectId(changeOrderDTO.getCategoryId());
        order.getRoom().setObjectId(orderDTO.getRoomId());
        order.setSum(orderDTO.getLivingCost() + orderDTO.getCategoryCost());
        order.setLastModificator(lastModificator);
		order.setLivingStartDate(orderDTO.getArrival());
        order.setLivingFinishDate(orderDTO.getDeparture());
        order.getRoom().getRoomType().setObjectId(changeOrderDTO.getRoomTypeId());
        return updateOrder(orderId, order);
    }

    @Transactional(readOnly = true)
    @Override
    public Order getSingleOrderById(Integer id) {
        Order order = orderDAO.getOrder(id);
        if (order == null) {
            LOGGER.info("Returned NULL!!!");
        }
        return order;
    }

    @Transactional
    @Override
    public IUDAnswer deleteOrder(Integer id) {
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        orderDAO.deleteOrder(id);

        return new IUDAnswer(id, true);
    }

    @Transactional
    @Override
    public IUDAnswer insertOrder(Order order) {
        if (order == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        boolean isValidDates = serviceUtils.checkDates(order.getLivingStartDate(), order.getLivingFinishDate());
        if (!isValidDates) {
            return new IUDAnswer(false, WRONG_DATES);
        }
        Integer orderId = orderDAO.insertOrder(order);

        return new IUDAnswer(orderId,true, ORDER_CREATED);
    }

    @Transactional
    @Override
    public IUDAnswer updateOrder(Integer id, Order newOrder) {
        if (newOrder == null) {
            return new IUDAnswer(false, NULL_ENTITY);
        }
        if (id == null) {
            return new IUDAnswer(false, NULL_ID);
        }
        boolean isValidDates = serviceUtils.checkDatesForUpdate(newOrder.getLivingStartDate(), newOrder.getLivingFinishDate());
        if (!isValidDates) {
            return new IUDAnswer(false, WRONG_DATES);
        }
        newOrder.setObjectId(id);
        Order oldOrder = orderDAO.getOrder(id);
        orderDAO.updateOrder(newOrder, oldOrder);

        return new IUDAnswer(id,true);
    }
}
