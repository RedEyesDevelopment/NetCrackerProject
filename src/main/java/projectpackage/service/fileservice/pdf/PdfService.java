package projectpackage.service.fileservice.pdf;

import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.service.MessageBook;

import java.io.File;
import java.util.List;

public interface PdfService extends MessageBook{
    public File createOrderPDF(Order order, String path);
    public File createRoomStatisticPDF(List<Room> freeRooms, List<Order> orders, String path);
    public void deletePDF(String path);
}
