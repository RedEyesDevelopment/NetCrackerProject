package projectpackage.service.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import projectpackage.model.orders.Order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Gvozd on 09.04.2017.
 */
@Service
public class PdfServiceImpl implements PdfService{
    private final String SITE_URL = "https://www.youtube.com/watch?v=e2cx7hk-JQA&feature=youtu.be&t=8";//ЮРЛ нашего сайта, в будущем
    private final String URL_PHOTOS = "D:\\AndProjects\\Netcracker\\NetCrackerProject\\src\\main\\resources\\photos\\";//ЮРЛ всех наших фоток

    @Override
    public String createPDF(Order order) throws DocumentException, IOException {
        String urlPhoto;
        if (order.getRoom().getRoomType().getRoomTypeTitle().equals("Luxe")) {
         urlPhoto = URL_PHOTOS + "Luxe.jpg";
        } else if (order.getRoom().getRoomType().getRoomTypeTitle().equals("Semi-Luxe")) {
         urlPhoto = URL_PHOTOS + "Luxe.jpg";
        } else if (order.getRoom().getRoomType().getRoomTypeTitle().equals("Economy")) {
         urlPhoto = URL_PHOTOS + "Luxe.jpg";
        } else if (order.getRoom().getRoomType().getRoomTypeTitle().equals("President")) {
         urlPhoto = URL_PHOTOS + "Luxe.jpg";
        } else {
         urlPhoto = URL_PHOTOS + "NotFound.jpg";
        }

        String pathToPDF = "D:\\Order#" + order.getObjectId() + ".pdf";
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathToPDF));
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

        Image roomTypeImage = Image.getInstance(urlPhoto);
        roomTypeImage.scaleAbsolute(300f, 200f);
        roomTypeImage.setIndentationLeft(100);

        Paragraph contentText = new Paragraph(order.getComment());
        contentText.setSpacingBefore(70);

        Paragraph lease = new Paragraph("Living date :\n" + order.getLivingStartDate() + "  -  " + order.getLivingFinishDate());
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

        document.add(chapter);
        document.close();

        return pathToPDF;
   }

    @Override
    public void deletePDF(String path) {
        File file = new File(path);
        file.delete();
    }
}
