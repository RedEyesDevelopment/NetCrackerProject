package projectpackage.controllers;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projectpackage.model.orders.Order;
import projectpackage.service.fileservice.mails.MailService;
import projectpackage.service.fileservice.pdf.PdfService;
import projectpackage.service.orderservice.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Created by Lenovo on 06.06.2017.
 */
@RestController
@RequestMapping("/statistics")

public class FileController {

    @Autowired
    OrderService orderService;

    @Autowired
    PdfService pdfService;

    @Autowired
    MailService mailService;

    @RequestMapping(value = "/order", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void getSearchForm(HttpServletRequest request){
        String path = request.getServletContext().getRealPath("/").toString();
        Order order = orderService.getSingleOrderById(300);
        File file = null;
        try {
             file = pdfService.createOrderPDF(order ,path);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mailService.sendEmailWithAttachment("denis.yakimov89@gmail.com", 1, file);
    }
}
