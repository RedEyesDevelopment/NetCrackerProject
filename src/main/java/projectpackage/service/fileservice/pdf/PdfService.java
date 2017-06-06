package projectpackage.service.fileservice.pdf;

import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;

import java.io.File;
import java.util.List;

/**
 * Created by Arizel on 21.05.2017.
 */
public interface PdfService {
    public File createOrderPDF(Order order, String path);
    public File createRoomStatisticPDF(List<Room> freeRooms, List<Order> orders, String path);
    public void deletePDF(String path);
}
