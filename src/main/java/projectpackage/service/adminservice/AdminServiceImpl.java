package projectpackage.service.adminservice;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.service.fileservice.pdf.PdfService;
import projectpackage.service.orderservice.OrderService;
import projectpackage.service.roomservice.RoomService;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Log4j
@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    RoomService roomService;

    @Autowired
    OrderService orderService;

    @Autowired
    PdfService pdfService;

    @Override
    public File getStatistic(String path) {
        Date today = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        List<Room> freeRoom = roomService.getFreeRoomsOnPeriod(today, calendar.getTime());
        freeRoom.sort((o1, o2) -> o1.getRoomNumber() - o2.getRoomNumber());

        List<Order> orders = orderService.getOrdersInRange(today, today);
        orders.sort((o1, o2) -> (int) (o1.getRegistrationDate().getTime() - o2.getRegistrationDate().getTime()));
        File statisticPDF = pdfService.createRoomStatisticPDF(freeRoom, orders, path);

        return statisticPDF;
    }
}
