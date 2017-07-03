package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.NotificationDTO;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.model.notifications.NotificationType;
import projectpackage.model.orders.Order;
import projectpackage.service.notificationservice.NotificationService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;

/**
 * Created by Lenovo on 28.05.2017.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    ServiceUtils serviceUtils;

    //Get Notification List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "notificationList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Notification>> getNotificationList() {
        List<Notification> notifications = notificationService.getAllNotifications();
        List<Resource<Notification>> resources = new ArrayList<>();
        for (Notification notification : notifications) {
            Resource<Notification> notificationResource = new Resource<Notification>(notification);
            notificationResource.add(linkTo(methodOn(NotificationController.class).getNotification(notification.getObjectId(), null)).withSelfRel());
            resources.add(notificationResource);
        }
        return resources;
    }

    //Get Notification List
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/current",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Notification>> getNotExecutedNotificationList() {
        List<Notification> notifications = notificationService.getAllNotExecutedNotifications();
        List<Resource<Notification>> resources = new ArrayList<>();
        for (Notification notification : notifications) {
            Resource<Notification> notificationResource = new Resource<Notification>(notification);
            notificationResource.add(linkTo(methodOn(NotificationController.class).getNotification(notification.getObjectId(), null)).withSelfRel());
            resources.add(notificationResource);
        }
        return resources;
    }

    @RequestMapping(value = "/execute/{id}",method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> makeExecuted(@PathVariable("id") Integer id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminReceptionAndData(user, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Notification notification = notificationService.getSingleNotificationById(id);
        notification.setExecutedDate(new Date());
        notification.setExecutedBy(user);
        IUDAnswer answer = notificationService.updateNotification(id, notification);
        if (!answer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
        } else {
           return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
        }
    }

    //Get single Notification by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Notification>> getNotification(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        if (thisUser.getRole().getObjectId() == 3) {
            return new ResponseEntity<Resource<Notification>>(new Resource<Notification>(new Notification()), HttpStatus.BAD_REQUEST);
        }
        Notification notification = notificationService.getSingleNotificationById(id);
        Resource<Notification> resource = new Resource<>(notification);
        HttpStatus status;
        if (null!= notification){
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Notification>> response = new ResponseEntity<Resource<Notification>>(resource, status);
        return response;
    }

    @CacheRemoveAll(cacheName = "notificationList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createNotification(@RequestBody NotificationDTO notificationDTO, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        if (user == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NEED_TO_AUTH), HttpStatus.BAD_REQUEST);
        } else if (user.getRole().getObjectId() == 3) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NOT_RECEPTION_OR_ADMIN), HttpStatus.BAD_REQUEST);
        } else if (notificationDTO == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ENTITY), HttpStatus.BAD_REQUEST);
        }

        NotificationType notificationType = new NotificationType();
        notificationType.setObjectId(notificationDTO.getNotificationTypeId());
        User author = new User();
        author.setObjectId(notificationDTO.getAuthorId());
        Order order = new Order();
        order.setObjectId(notificationDTO.getOrderId());
        Notification newNotification = new Notification();
        newNotification.setMessage(notificationDTO.getMessage());
        newNotification.setNotificationType(notificationType);
        newNotification.setOrder(order);
        newNotification.setAuthor(author);
        IUDAnswer result = notificationService.insertNotification(newNotification);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @CacheRemoveAll(cacheName = "notificationList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateNotification(@PathVariable("id") Integer id, @RequestBody NotificationDTO notificationDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        if (sessionUser == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NEED_TO_AUTH), HttpStatus.BAD_REQUEST);
        } else if (sessionUser.getRole().getObjectId() == 3) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NOT_RECEPTION_OR_ADMIN), HttpStatus.BAD_REQUEST);
        } else if (id == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, NULL_ID), HttpStatus.BAD_REQUEST);
        } else if (notificationDTO == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ENTITY), HttpStatus.BAD_REQUEST);
        }
        Notification changedNotification = notificationService.getSingleNotificationById(id);
        changedNotification.getAuthor().setObjectId(notificationDTO.getAuthorId());
        changedNotification.setMessage(notificationDTO.getMessage());
        changedNotification.getNotificationType().setObjectId(notificationDTO.getNotificationTypeId());
        changedNotification.getOrder().setObjectId(notificationDTO.getOrderId());
        IUDAnswer result = notificationService.updateNotification(id, changedNotification);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @CacheRemoveAll(cacheName = "notificationList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteNotification(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        if (sessionUser == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NEED_TO_AUTH), HttpStatus.BAD_REQUEST);
        } else if (sessionUser.getRole().getObjectId() == 3) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NOT_RECEPTION_OR_ADMIN), HttpStatus.BAD_REQUEST);
        } else if (id == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, DISCREPANCY_PARENT_ID), HttpStatus.BAD_REQUEST);
        }
        IUDAnswer result = notificationService.deleteNotification(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
