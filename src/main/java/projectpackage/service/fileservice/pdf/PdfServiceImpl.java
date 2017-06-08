package projectpackage.service.fileservice.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.service.fileservice.files.DateFileNameGenerator;
import projectpackage.service.fileservice.files.OrderFileNameGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Gvozd on 09.04.2017.
 */
@Service
public class PdfServiceImpl implements PdfService{
    private static final Logger LOGGER = Logger.getLogger(PdfServiceImpl.class);

    private final String SITE_URL = "https://www.youtube.com/watch?v=e2cx7hk-JQA&feature=youtu.be&t=8";//ЮРЛ нашего сайта, в будущем
    //private final String URL_PHOTOS = "D:\\AndProjects\\Netcracker\\NetCrackerProject\\src\\main\\resources\\photos\\";//ЮРЛ всех наших фоток
    private final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    OrderFileNameGenerator orderFileNameGenerator;

    @Autowired
    DateFileNameGenerator dateFileNameGenerator;

    @Override
    public File createOrderPDF(Order order, String path) {
        String urlPhoto;
        if (order.getRoom().getRoomType().getRoomTypeTitle().equals("Luxe")) {
         urlPhoto = "static/Luxe.jpg";
        } else if (order.getRoom().getRoomType().getRoomTypeTitle().equals("Semi-Luxe")) {
         urlPhoto = "static/Luxe.jpg";
        } else if (order.getRoom().getRoomType().getRoomTypeTitle().equals("Economy")) {
         urlPhoto = "static/Luxe.jpg";
        } else if (order.getRoom().getRoomType().getRoomTypeTitle().equals("President")) {
         urlPhoto = "static/Luxe.jpg";
        } else {
         urlPhoto = "static/NotFound.jpg";
        }

//        String pathToPDF = "D:\\Order#" + order.getObjectId() + ".pdf";
        String pathToPDF = path+"\\pdfs\\orders\\"+orderFileNameGenerator.generateFileName("OrderPayment",order.getObjectId())+".pdf";
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pathToPDF));
        } catch (DocumentException e) {
            LOGGER.warn("Error with orderPDF:" + e.getMessage());
            return null;
        } catch (FileNotFoundException e) {
            LOGGER.warn("Error with orderPDF:" + e.getMessage());
            return null;
        }
        document.open();

        Chapter chapter = new Chapter(1);
        //chapter.setNumberDepth(0);

        Paragraph dearUser = new Paragraph("Dear " + order.getClient().getFirstName() +
                " " + order.getClient().getLastName() + "!");
        dearUser.setIndentationLeft(100);

        Paragraph acceptanceOrder = new Paragraph("Your order accepted!");
        acceptanceOrder.setIndentationLeft(100);

        Paragraph userRoomType = new Paragraph("Your room of class " + order.getRoom().getRoomType().getRoomTypeTitle());
        userRoomType.setIndentationLeft(100);

        Image roomTypeImage = null;
        try {
            roomTypeImage = Image.getInstance(urlPhoto);
        } catch (BadElementException e) {
            LOGGER.warn("Error with orderPDF:" + e.getMessage());
            return null;
        } catch (IOException e) {
            LOGGER.warn("Error with orderPDF:" + e.getMessage());
            return null;
        }
        roomTypeImage.scaleAbsolute(300f, 200f);
        roomTypeImage.setIndentationLeft(100);

        Paragraph contentText = new Paragraph(order.getComment());
        contentText.setSpacingBefore(70);

        Paragraph lease = new Paragraph("Living date :\n" + df.format(order.getLivingStartDate())
                + "  -  " + df.format(order.getLivingFinishDate()));
        lease.setSpacingBefore(50);

        Paragraph priceText = new Paragraph("Price : " + order.getSum());
        priceText.setSpacingBefore(10);

        Paragraph smallInfo = new Paragraph("Pay up to 3am!");
        smallInfo.setSpacingBefore(70);

        Anchor toSite = new Anchor("Url our site!");
        toSite.setReference(SITE_URL);

        Section section = chapter.addSection(dearUser);
        section.add(acceptanceOrder);
        section.add(userRoomType);
        section.add(roomTypeImage);
        section.add(contentText);
        section.add(lease);
        section.add(priceText);
        section.add(smallInfo);
        section.add(toSite);

        try {
            document.add(chapter);
        } catch (DocumentException e) {
            LOGGER.warn("Error with orderPDF:" + e.getMessage());
            return null;
        }
        document.close();

        File file = new File(pathToPDF);
        return file;
    }

    @Override
    public File createRoomStatisticPDF(List<Room> freeRooms, List<Order> orders, String path) {

        String pathToPDF = path+"\\pdfs\\statistics\\"+ dateFileNameGenerator.generateFileNameWithCurrentTime("Statistics") + ".pdf";
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pathToPDF));
        } catch (DocumentException e) {
            LOGGER.warn("Error with statisticPDF:" + e.getMessage());
            return null;
        } catch (FileNotFoundException e) {
            LOGGER.warn("Error with statisticPDF:" + e.getMessage());
            return null;
        }
        document.open();

        Chapter chapter = new Chapter("Table of free rooms", 1);

        PdfPTable tableOfRoom = new PdfPTable(3);

        tableOfRoom.setSpacingBefore(25);
        tableOfRoom.setSpacingAfter(25);
        PdfPCell roomNumberCell = new PdfPCell(new Phrase("Room number"));
        tableOfRoom.addCell(roomNumberCell);
        PdfPCell roomTypeCell = new PdfPCell(new Phrase("Room type"));
        tableOfRoom.addCell(roomTypeCell);
        PdfPCell numberOfResidentsCell = new PdfPCell(new Phrase("Number of residents"));
        tableOfRoom.addCell(numberOfResidentsCell);

        for (Room room : freeRooms) {
            tableOfRoom.addCell(String.valueOf(room.getRoomNumber()));
            tableOfRoom.addCell(room.getRoomType().getRoomTypeTitle());
            tableOfRoom.addCell(String.valueOf(room.getNumberOfResidents()));
        }

        Section sectionTable = chapter.addSection("Table of free rooms");
        sectionTable.add(tableOfRoom);

        try {
            document.add(chapter);
        } catch (DocumentException e) {
            LOGGER.warn("Error with statisticPDF:" + e.getMessage());
            return null;
        }

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);

            Chapter orderChapter = new Chapter(new Paragraph("Order for room №" + order.getRoom().getRoomNumber())
                    , i + 1);

            Paragraph orderId = new Paragraph("Order ID: " + order.getObjectId());
            Paragraph userRoomType = new Paragraph("Room of class " + order.getRoom().getRoomType().getRoomTypeTitle());
            Paragraph registrationDate = new Paragraph("Registration date: " + df.format(order.getRegistrationDate()));
            Paragraph isConfirmed = new Paragraph("Is confirmed: " + order.getIsConfirmed());
            Paragraph isPaidFor = new Paragraph("Is paid for: " + order.getIsPaidFor());
            Paragraph contentText = new Paragraph("Comment: " + order.getComment());
            Paragraph lease = new Paragraph("Living date : "
                    + df.format(order.getLivingStartDate()) + "  -  " + df.format(order.getLivingFinishDate()));
            Paragraph priceText = new Paragraph("Price : " + order.getSum()/100);

            PdfPTable tableOfLastModificator = new PdfPTable(4);
            tableOfLastModificator.setSpacingBefore(25);
            tableOfLastModificator.setSpacingAfter(25);

            PdfPCell nameModificatorCell = new PdfPCell(new Phrase("Last modificator name"));
            tableOfLastModificator.addCell(nameModificatorCell);
            PdfPCell getEnabledCell = new PdfPCell(new Phrase("Is enable"));
            tableOfLastModificator.addCell(getEnabledCell);
            PdfPCell emailCell = new PdfPCell(new Phrase("Email"));
            tableOfLastModificator.addCell(emailCell);
            PdfPCell additionalInfoCell = new PdfPCell(new Phrase("Additional info"));
            tableOfLastModificator.addCell(additionalInfoCell);

            User user = order.getLastModificator();
            tableOfLastModificator.addCell(user.getFirstName() + " " + user.getLastName());
            tableOfLastModificator.addCell(String.valueOf(user.getEnabled()));
            tableOfLastModificator.addCell(user.getEmail());
            tableOfLastModificator.addCell(user.getAdditionalInfo());

            PdfPTable tableOfCategory = new PdfPTable(2);
            tableOfCategory.setSpacingBefore(25);
            tableOfCategory.setSpacingAfter(25);

            PdfPCell categoryTitleCell = new PdfPCell(new Phrase("Category title"));
            tableOfCategory.addCell(categoryTitleCell);
            PdfPCell categoryPriceCell = new PdfPCell(new Phrase("Category Price"));
            tableOfCategory.addCell(categoryPriceCell);

            Category category = order.getCategory();
            tableOfCategory.addCell(category.getCategoryTitle());
            tableOfCategory.addCell(String.valueOf(category.getCategoryPrice()/100));

            Section section = orderChapter.addSection("Order stats");
            section.add(orderId);
            section.add(registrationDate);
            section.add(isConfirmed);
            section.add(isPaidFor);
            section.add(userRoomType);
            section.add(contentText);
            section.add(lease);
            section.add(priceText);

            orderChapter.addSection("Table of Last Modificator");
            orderChapter.add(tableOfLastModificator);
            orderChapter.addSection("Table of Category");
            orderChapter.add(tableOfCategory);

            try {
                document.add(orderChapter);
            } catch (DocumentException e) {
                LOGGER.warn("Error with statisticPDF:" + e.getMessage());
                return null;
            }
        }

        document.close();

        return new File(pathToPDF);
    }

    @Override
    public void deletePDF(String path) {
        File file = new File(path);
        file.delete();
    }
}