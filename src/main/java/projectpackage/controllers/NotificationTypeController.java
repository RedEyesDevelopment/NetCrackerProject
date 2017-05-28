package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.NotificationType;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.notificationservice.NotificationTypeService;

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
@RequestMapping("/notificationtypes")
public class NotificationTypeController {

    @Autowired
    NotificationTypeService notificationTypeService;

    //Get NotificationType List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "notificationTypeList")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<NotificationType>> getNotificationTypeList(){
        List<NotificationType> notificationTypes = notificationTypeService.getAllNotificationTypes();
        List<Resource<NotificationType>> resources = new ArrayList<>();
        for (NotificationType notificationType:notificationTypes){
            Resource<NotificationType> notificationTypeResource = new Resource<NotificationType>(notificationType);
            notificationTypeResource.add(linkTo(methodOn(NotificationTypeController.class).getNotificationType(notificationType.getObjectId(), null)).withSelfRel());
            resources.add(notificationTypeResource);
        }
        return resources;
    }

    //Get single NotificationType by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<NotificationType>> getNotificationType(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        NotificationType notificationType = notificationTypeService.getSingleNotificationTypeById(id);
        Resource<NotificationType> resource = new Resource<>(notificationType);
        HttpStatus status;
        if (null!= notificationType){
            if (thisUser.getRole().getRoleName().equals("ADMIN")) resource.add(linkTo(methodOn(NotificationTypeController.class).deleteNotificationType(notificationType.getObjectId())).withRel("delete"));
            resource.add(linkTo(methodOn(NotificationTypeController.class).updateNotificationType(notificationType.getObjectId(), notificationType)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<NotificationType>> response = new ResponseEntity<Resource<NotificationType>>(resource, status);
        return response;
    }

    //Create notificationType, fetch into database
    @CacheRemoveAll(cacheName = "notificationTypeList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createNotificationType(@RequestBody NotificationType newNotificationType){
        IUDAnswer result = notificationTypeService.insertNotificationType(newNotificationType);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update notificationType method
//    	@Secured("ROLE_ADMIN")
    @CacheRemoveAll(cacheName = "notificationTypeList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateNotificationType(@PathVariable("id") Integer id, @RequestBody NotificationType changedNotificationType){
        if (!id.equals(changedNotificationType.getObjectId())){
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, "wrongId"), HttpStatus.NOT_ACCEPTABLE);
        }
        IUDAnswer result = notificationTypeService.updateNotificationType(id, changedNotificationType);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete notificationType method
    @CacheRemoveAll(cacheName = "notificationTypeList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteNotificationType(@PathVariable("id") Integer id){
        IUDAnswer result = notificationTypeService.deleteNotificationType(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}