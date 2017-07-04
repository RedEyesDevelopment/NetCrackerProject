package projectpackage.service.fileservice.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Category;
import projectpackage.model.orders.Order;
import projectpackage.model.rooms.Room;
import projectpackage.service.fileservice.files.DateFileNameGenerator;
import projectpackage.service.fileservice.files.OrderFileNameGenerator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService{
    private static final Logger LOGGER = Logger.getLogger(PdfServiceImpl.class);
    private final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    OrderFileNameGenerator orderFileNameGenerator;

    @Autowired
    DateFileNameGenerator dateFileNameGenerator;

    @Override
    public File createOrderPDF(Order order, String path) {
        String pathToPDF = path + "\\pdfs\\orders\\" + orderFileNameGenerator
                .generateFileName("OrderPayment",order.getObjectId()) + ".pdf";
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(new FileInputStream(path + "\\pdfs\\orders\\template.pdf"));
            stamper = new PdfStamper(reader, new FileOutputStream(pathToPDF));
        } catch (IOException e) {
            LOGGER.warn(PDF_ERROR, e);
            return null;
        } catch (DocumentException e) {
            LOGGER.warn(PDF_ERROR, e);
            return null;
        }

        PdfContentByte stream = stamper.getOverContent(1);
        stream.beginText();
        stream.setColorFill(BaseColor.WHITE);

        BaseFont font = null;
        try {
            font = BaseFont.createFont();
        } catch (DocumentException e) {
            LOGGER.warn(PDF_ERROR, e);
            return null;
        } catch (IOException e) {
            LOGGER.warn(PDF_ERROR, e);
            return null;
        }
        stream.setFontAndSize(font, 16);

        stream.setTextMatrix(185, 540);
        stream.showText(String.valueOf(order.getObjectId()));
        stream.setTextMatrix(185, 490);
        stream.showText(String.valueOf(order.getRoom().getRoomNumber()));
        stream.setTextMatrix(220, 438);
        stream.showText(order.getRoom().getRoomType().getRoomTypeTitle());
        stream.setTextMatrix(190, 280);
        stream.showText(order.getCategory().getCategoryTitle());
        stream.setTextMatrix(175, 243);
        stream.showText(df.format(order.getLivingStartDate()) + "  -  " + df.format(order.getLivingFinishDate()));
        stream.setTextMatrix(125, 205);
        stream.showText(order.getSum()/100   + " $  " + order.getSum()%100 + " \u00A2");
        stream.endText();

        try {
            stamper.setFullCompression();
            stamper.close();
        } catch (DocumentException e) {
            LOGGER.warn(PDF_ERROR, e);
            return null;
        } catch (IOException e) {
            LOGGER.warn(PDF_ERROR, e);
            return null;
        }
        return new File(pathToPDF);
    }

    @Override
    public File createRoomStatisticPDF(List<Room> freeRooms, List<Order> orders, String path) {
        String pathToPDF = path+"\\pdfs\\statistics\\"+ dateFileNameGenerator.generateFileNameWithCurrentTime("Statistics") + ".pdf";
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pathToPDF));
        } catch (DocumentException e) {
            LOGGER.warn(PDF_ERROR, e);
            return null;
        } catch (FileNotFoundException e) {
            LOGGER.warn(PDF_ERROR, e);
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
            LOGGER.warn(PDF_ERROR, e);
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
            Paragraph priceText = new Paragraph("Price : " + order.getSum()/100 + "." + order.getSum()%100 + " $");

            PdfPTable tableOfLastModificator = new PdfPTable(4);
            tableOfLastModificator.setWidthPercentage(100);
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
            tableOfCategory.setWidthPercentage(100);
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
                LOGGER.warn(PDF_ERROR, e);
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
