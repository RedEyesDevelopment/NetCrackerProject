package projectpackage.service.orderservice;

import projectpackage.dto.ChangeOrderDTO;
import projectpackage.dto.FreeRoomsUpdateOrderDTO;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.OrderDTO;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.service.MessageBook;

import java.util.Date;
import java.util.List;

public interface OrderService extends MessageBook{
    public List<Order> getSimpleOrderList();
    List<Order> getOrdersByRoom(Room room);
    List<Order> getOrdersByClient(User user);
    List<Order> getOrdersByRegistrationDate(Date date);
    List<Order> getCurrentOrders();//livingStartDate < SYSDATE < livingFinishDate ясно?
    List<Order> getPreviousOrders();//livingFinishDate < SYSDATE
    List<Order> getFutureOrders();//livingStartDate > SYSDATE
    List<Order> getOrdersForPayConfirmed();
    List<Order> getOrdersInRange(Date startDate, Date finishDate);
    List<Order> getOrdersConfirmed();
    List<Order> getOrdersMustToBePaid();
    IUDAnswer createOrder(User client, int roomTypeId, int numberOfResidents, Date start, Date finish, Category
            category, long summ);
    List<Order> getAllOrderForAdmin();
    Order getOrderForAdmin(Integer id);
    Order createOrderTemplate(User client, User lastModificator, OrderDTO dto);
    List<Order> getAllOrders();
    IUDAnswer setNewDataIntoOrder(Integer orderId, Integer lastModificatorId, ChangeOrderDTO changeOrderDTO, OrderDTO orderDTO);
    FreeRoomsUpdateOrderDTO getFreeRoomsToUpdateOrder(Integer orderId, ChangeOrderDTO changeOrderDTO);
    Order getSingleOrderById(Integer id);
    IUDAnswer deleteOrder(Integer id);
    IUDAnswer insertOrder(Order order);
    IUDAnswer updateOrder(Integer id, Order newOrder);
}
