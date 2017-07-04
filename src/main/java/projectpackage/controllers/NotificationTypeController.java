package projectpackage.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.NotificationTypeDTO;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.model.notifications.NotificationType;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.MessageBook;
import projectpackage.service.notificationservice.NotificationTypeService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;

/**
 * Created by Lenovo on 28.05.2017.
 */
@RestController
@RequestMapping("/notificationtypes")
public class NotificationTypeController {

    private static final Logger LOGGER = Logger.getLogger(NotificationTypeController.class);

    @Autowired
    NotificationTypeService notificationTypeService;

    @Autowired
    ServiceUtils serviceUtils;

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
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<NotificationType>> getNotificationType(@PathVariable("id") Integer id, HttpServletRequest request){
        NotificationType notificationType = notificationTypeService.getSingleNotificationTypeById(id);
        Resource<NotificationType> resource = new Resource<>(notificationType);
        HttpStatus status;
        if (null!= notificationType){
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<NotificationType>> response = new ResponseEntity<Resource<NotificationType>>(resource, status);
        return response;
    }

    //Create notificationType, fetch into database
    @CacheRemoveAll(cacheName = "notificationTypeList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createNotificationType(@RequestBody NotificationTypeDTO notificationTypeDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, notificationTypeDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        if (notificationTypeDTO == null || notificationTypeDTO.getOrientedRole() == null
                || notificationTypeDTO.getNotificationTypeTitle() == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ENTITY), HttpStatus.BAD_REQUEST);
        }
        NotificationType newNotificationType = new NotificationType();
        Role role = new Role();
        role.setObjectId(notificationTypeDTO.getOrientedRole());
        newNotificationType.setOrientedRole(role);
        newNotificationType.setNotificationTypeTitle(notificationTypeDTO.getNotificationTypeTitle());
        try {
            iudAnswer = notificationTypeService.insertNotificationType(newNotificationType);
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

    //Update notificationType method
//    	@Secured("ROLE_ADMIN")
    @CacheRemoveAll(cacheName = "notificationTypeList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateNotificationType(@PathVariable("id") Integer id,
                                                            @RequestBody NotificationTypeDTO notificationTypeDTO,
                                                            HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, notificationTypeDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        if (id == null || notificationTypeDTO.getOrientedRole() == null || notificationTypeDTO.getNotificationTypeTitle() == null){
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, MessageBook.WRONG_UPDATE_ID), HttpStatus.NOT_ACCEPTABLE);
        }

        Role role = new Role();
        role.setObjectId(notificationTypeDTO.getOrientedRole());
        NotificationType changedNotificationType = new NotificationType();
        changedNotificationType.setObjectId(id);
        changedNotificationType.setNotificationTypeTitle(notificationTypeDTO.getNotificationTypeTitle());
        changedNotificationType.setOrientedRole(role);

        try {
            iudAnswer = notificationTypeService.updateNotificationType(id, changedNotificationType);
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

    //Delete notificationType method
    @CacheRemoveAll(cacheName = "notificationTypeList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteNotificationType(@PathVariable("id") Integer id, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        try {
            iudAnswer = notificationTypeService.deleteNotificationType(id);
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
