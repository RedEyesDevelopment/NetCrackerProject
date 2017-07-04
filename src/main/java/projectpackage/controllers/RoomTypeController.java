package projectpackage.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.JacksonMappingMarker;
import projectpackage.dto.RoomTypeDTO;
import projectpackage.model.auth.User;
import projectpackage.model.rooms.RoomType;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.roomservice.RoomTypeService;
import projectpackage.service.support.ServiceUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;

@RestController
@RequestMapping("/roomTypes")
public class RoomTypeController {

    private static final Logger LOGGER = Logger.getLogger(RoomTypeController.class);

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    ServiceUtils serviceUtils;

    //Get RoomType List
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<RoomType>> getRoomTypeList(){
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypes();
        List<Resource<RoomType>> resources = new ArrayList<>();
        for (RoomType roomType:roomTypes){
            Resource<RoomType> roomTypeResource = new Resource<RoomType>(roomType);
            roomTypeResource.add(linkTo(methodOn(RoomTypeController.class).getRoomType(roomType.getObjectId(), null)).withSelfRel());
            resources.add(roomTypeResource);
        }
        return resources;
    }

    @JsonView(JacksonMappingMarker.List.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/simpleList", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<RoomType> getSimpleRoomTypeList(){
        return roomTypeService.getAllRoomTypes();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<RoomType>> getRoomType(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        RoomType roomType = roomTypeService.getSingleRoomTypeById(id);
        Resource<RoomType> resource = new Resource<>(roomType);
        HttpStatus status;
        if (null != roomType){
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<RoomType>> response = new ResponseEntity<Resource<RoomType>>(resource, status);
        return response;
    }

    //Create roomType, fetch into database
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createRoomType(@RequestBody RoomTypeDTO roomTypeDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, roomTypeDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        RoomType roomType = new RoomType();
        roomType.setRoomTypeTitle(roomTypeDTO.getRoomTypeTitle());
        roomType.setContent(roomTypeDTO.getRoomTypeContent());
        try {
            iudAnswer = roomTypeService.insertRoomType(roomType);
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

    //Update roomType method
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateRoomType(@PathVariable("id") Integer id, @RequestBody RoomTypeDTO roomTypeDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, roomTypeDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        RoomType roomType = new RoomType();
        roomType.setObjectId(id);
        roomType.setContent(roomTypeDTO.getRoomTypeContent());
        roomType.setRoomTypeTitle(roomTypeDTO.getRoomTypeTitle());
        try {
            iudAnswer = roomTypeService.updateRoomType(id, roomType);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, WRONG_FIELD, e.getMessage()), HttpStatus.BAD_REQUEST);
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

    //Delete roomType method
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteRoomType(@PathVariable("id") Integer id, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        User user = (User) request.getSession().getAttribute("USER");

        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);

        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, status);
        }

        try {
            iudAnswer = roomTypeService.deleteRoomType(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage()), status);
        } catch (DeletedObjectNotExistsException e) {
            LOGGER.warn(DELETED_OBJECT_NOT_EXISTS, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, DELETED_OBJECT_NOT_EXISTS, e.getMessage()), status);
        } catch (WrongEntityIdException e) {
            LOGGER.warn(WRONG_DELETED_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, WRONG_DELETED_ID, e.getMessage()), status);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(NULL_ID, e);
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, NULL_ID, e.getMessage()), status);
        }
        if (iudAnswer.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }

        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(iudAnswer, status);
        return responseEntity;
    }
}
