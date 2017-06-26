package tests.pdf;

import com.itextpdf.text.DocumentException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.model.rooms.RoomType;
import projectpackage.service.adminservice.AdminService;
import projectpackage.service.fileservice.pdf.PdfService;

import java.io.File;
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
        order.setObjectId(573489578);
        order.setRegistrationDate(new Date());
        order.setIsPaidFor(false);
        order.setIsConfirmed(false);
        order.setLivingStartDate(new Date());
        order.setLivingFinishDate(new Date());
        order.setSum(7478L);
        order.setComment("CommentTTTTTTTTTTTTTTTTT");
        order.setRoom(room);
        order.setClient(user);
        Category category = new Category();
        category.setCategoryTitle("My category");
        order.setCategory(category);
        File file = pdfService.createOrderPDF(order, "D:\\AndProjects\\Netcracker\\NetCrackerProject\\src\\main\\webapp");
        LOGGER.info(file);
        LOGGER.info(SEPARATOR);
    }

    @Test
    public void testStatPDFCreation() {
        File file = adminService.getStatistic("");
        Assert.assertNotNull(file);
        LOGGER.info(file);
    }

    @Test
    public void deletePDF() {
        pdfService.deletePDF("D:\\Order#0.pdf");
    }
}
