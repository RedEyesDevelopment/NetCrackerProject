package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.Notification;
import projectpackage.service.MessageBook;
import projectpackage.service.notificationservice.NotificationService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Lenovo on 28.05.2017.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

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

    //Get single Notification by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Notification>> getNotification(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        Notification notification = notificationService.getSingleNotificationById(id);
        Resource<Notification> resource = new Resource<>(notification);
        HttpStatus status;
        if (null!= notification){
            if (thisUser.getRole().getRoleName().equals("ADMIN"))
                resource.add(linkTo(methodOn(NotificationController.class).deleteNotification(notification.getObjectId())).withRel("delete"));
            resource.add(linkTo(methodOn(NotificationController.class).updateNotification(notification.getObjectId(), notification)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Notification>> response = new ResponseEntity<Resource<Notification>>(resource, status);
        return response;
    }

    //Create notification, fetch into database
    @CacheRemoveAll(cacheName = "notificationList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createNotification(@RequestBody Notification newNotification) {
        IUDAnswer result = notificationService.insertNotification(newNotification);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update notification method
    @CacheRemoveAll(cacheName = "notificationList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateNotification(@PathVariable("id") Integer id, @RequestBody Notification changedNotification) {
        if (!id.equals(changedNotification.getObjectId())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, MessageBook.WRONG_UPDATE_ID), HttpStatus.BAD_REQUEST);
        }
        IUDAnswer result = notificationService.updateNotification(id, changedNotification);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete notification method
    //	@Secured("ROLE_ADMIN")
    @CacheRemoveAll(cacheName = "notificationList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteNotification(@PathVariable("id") Integer id) {
        IUDAnswer result = notificationService.deleteNotification(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
