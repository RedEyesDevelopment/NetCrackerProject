package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;
import projectpackage.model.orders.Order;
import projectpackage.service.adminservice.AdminService;
import projectpackage.service.fileservice.mails.MailService;
import projectpackage.service.fileservice.pdf.PdfService;
import projectpackage.service.orderservice.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static projectpackage.service.MessageBook.NEED_TO_AUTH;
import static projectpackage.service.MessageBook.NULL_ID;

@RestController
@RequestMapping("/pdf")
public class FileController {

    @Autowired
    OrderService orderService;

    @Autowired
    PdfService pdfService;

    @Autowired
    MailService mailService;

    @Autowired
    AdminService adminService;

    private DateFormat targetDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> sendOrderPdf(@PathVariable("id") Integer id, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NEED_TO_AUTH), HttpStatus.BAD_REQUEST);
        } else if (id == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ID), HttpStatus.BAD_REQUEST);
        }
        String path = request.getServletContext().getRealPath("/").toString();
        Order order = orderService.getSingleOrderById(id);
        File file = pdfService.createOrderPDF(order, path);
        mailService.sendEmailWithAttachment(order.getClient().getEmail(), 1, file);
        return new ResponseEntity<IUDAnswer>(new IUDAnswer(true), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> getStatistic(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        String path = request.getServletContext().getRealPath("/").toString();
        File file = adminService.getStatistic(path);
        String dates = targetDateFormat.format(new Date(System.currentTimeMillis()));
        mailService.sendEmailForStatistics(user.getEmail(), dates, file);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
}
