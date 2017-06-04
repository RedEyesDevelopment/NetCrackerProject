package tests.pdf;

import com.itextpdf.text.DocumentException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.service.adminservice.AdminService;
import projectpackage.service.pdf.PdfService;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;

/**
 * Created by Gvozd on 09.04.2017.
 */
public class PDFTest extends AbstractPDFTest {
    private static final Logger LOGGER = Logger.getLogger(PDFTest.class);

    @Autowired
    PdfService pdfService;

    @Autowired
    AdminService adminService;

    @Test
    public void testPDFCreation() throws IOException, DocumentException {
        Room room = new Room();
        RoomType roomType = new RoomType();
        roomType.setRoomTypeTitle("Luxe");
        room.setRoomType(roomType);
        room.setObjectId(127);
        User user = new User();
        user.setObjectId(900);
        user.setFirstName("Alex");
        user.setLastName("Merlyan");
        Order order = new Order();
        order.setRegistrationDate(new Date());
        order.setIsPaidFor(false);
        order.setIsConfirmed(false);
        order.setLivingStartDate(new Date());
        order.setLivingFinishDate(new Date());
        order.setSum(7478L);
        order.setComment("Comment");
        order.setRoom(room);
        order.setClient(user);
        String url = pdfService.createOrderPDF(order);
        LOGGER.info(url);
        LOGGER.info(SEPARATOR);
    }

    @Test
    public void testStatPDFCreation() {
        IUDAnswer iudAnswer = adminService.getStatistic();
        Assert.assertTrue(iudAnswer.isSuccessful());
        LOGGER.info(iudAnswer.getMessage());
    }

    @Test
    public void deletePDF() {
        pdfService.deletePDF("D:\\Order#0.pdf");
    }
}
