package projectpackage.controllers;

import org.apache.log4j.Logger;
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
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.notificationservice.NotificationService;
import projectpackage.service.support.ServiceUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;
import static projectpackage.service.MessageBook.NULL_ID;
import static projectpackage.service.MessageBook.WRONG_DELETED_ID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger LOGGER = Logger.getLogger(NotificationController.class);

    @Autowired
    NotificationService notificationService;

    @Autowired
    ServiceUtils serviceUtils;

    //Get Notification List
    @ResponseStatus(HttpStatus.OK)
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
        Notification notification = notificationService.getNotExecutedNotificationById(id);
        notification.setExecutedDate(new Date());
        notification.setExecutedBy(user);

        try {
            iudAnswer = notificationService.updateNotification(id, notification);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
        return responseEntity;
    }

    //Get single Notification by id
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
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Notification>> response = new ResponseEntity<Resource<Notification>>(resource, status);
        return response;
    }

    @RequestMapping(value = "/notexecuted/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Notification>> getNotExecutedNotification(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        if (thisUser.getRole().getObjectId() == 3) {
            return new ResponseEntity<Resource<Notification>>(new Resource<Notification>(new Notification()), HttpStatus.BAD_REQUEST);
        }
        Notification notification = notificationService.getNotExecutedNotificationById(id);
        Resource<Notification> resource = new Resource<>(notification);
        HttpStatus status;
        if (null!= notification){
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Notification>> response = new ResponseEntity<Resource<Notification>>(resource, status);
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createNotification(@RequestBody NotificationDTO notificationDTO, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminReceptionAndData(user, notificationDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
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

        try {
            iudAnswer = notificationService.insertNotification(newNotification);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
        return responseEntity;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateNotification(@PathVariable("id") Integer id, @RequestBody NotificationDTO notificationDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminReceptionAndData(sessionUser, notificationDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Notification changedNotification = notificationService.getNotExecutedNotificationById(id);
        changedNotification.getAuthor().setObjectId(notificationDTO.getAuthorId());
        changedNotification.setMessage(notificationDTO.getMessage());
        changedNotification.getNotificationType().setObjectId(notificationDTO.getNotificationTypeId());
        changedNotification.getOrder().setObjectId(notificationDTO.getOrderId());

        try {
            iudAnswer = notificationService.updateNotification(id, changedNotification);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
        return responseEntity;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteNotification(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdminAndReception(sessionUser, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }

        try {
            iudAnswer = notificationService.deleteNotification(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, NULL_ID, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        HttpStatus status;
        if (iudAnswer != null && iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return new ResponseEntity<IUDAnswer>(iudAnswer, status);
    }
}
