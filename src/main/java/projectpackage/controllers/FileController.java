package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projectpackage.model.orders.Order;
import projectpackage.service.adminservice.AdminService;
import projectpackage.service.fileservice.mails.MailService;
import projectpackage.service.fileservice.pdf.PdfService;
import projectpackage.service.orderservice.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

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

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/order", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void getSearchForm(HttpServletRequest request){
        String path = request.getServletContext().getRealPath("/").toString();
        Order order = orderService.getSingleOrderById(300);
        File file = pdfService.createOrderPDF(order, path);
        mailService.sendEmailWithAttachment("sashamerlyan@gmail.com", 1, file);
    }

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public File getStatistic(HttpServletRequest request){
        String path = request.getServletContext().getRealPath("/").toString();
        File file = adminService.getStatistic(path);
        mailService.sendEmailWithAttachment("fiendes03@gmail.com", 3, file);
        return file;
    }
}