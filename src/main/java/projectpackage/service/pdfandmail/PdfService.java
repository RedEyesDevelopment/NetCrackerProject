package projectpackage.service.pdfandmail;

import com.itextpdf.text.DocumentException;
import projectpackage.model.orders.Order;

import java.io.IOException;

/**
 * Created by Arizel on 21.05.2017.
 */
public interface PdfService {
    public String createPDF(Order order) throws DocumentException, IOException;
    public void deletePDF(String path);
}
