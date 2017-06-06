package projectpackage.service.fileservice.pdf;

import com.itextpdf.text.DocumentException;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Arizel on 21.05.2017.
 */
public interface PdfService {
    public File createOrderPDF(Order order, String path) throws DocumentException, IOException;
    public String createRoomStatisticPDF(List<Room> freeRooms, List<Order> orders) throws IOException, DocumentException;
    public void deletePDF(String path);
}
