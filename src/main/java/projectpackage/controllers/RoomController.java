package projectpackage.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.RoomDTO;
import projectpackage.model.auth.User;
import projectpackage.model.rooms.Room;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.roomservice.RoomService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private static final Logger LOGGER = Logger.getLogger(RoomController.class);

    @Autowired
    RoomService roomService;

    @Autowired
    ServiceUtils serviceUtils;

    //Get Room List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "roomList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Room>> getRoomList(){
        List<Room> rooms = roomService.getAllRooms();
        List<Resource<Room>> resources = new ArrayList<>();
        for (Room room:rooms){
            Resource<Room> roomResource = new Resource<Room>(room);
            roomResource.add(linkTo(methodOn(RoomController.class).getRoom(room.getObjectId(), null)).withSelfRel());
            resources.add(roomResource);
        }
        return resources;
    }

    //Get Room List
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/simpleList", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Room> getSimpleRoomList(){
        return roomService.getSimpleRoomList();
    }

    //Get Room List
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/simple/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Room> getSimpleRoom(@PathVariable("id") Integer id, HttpServletRequest request){
        Room room = roomService.getSimpleRoomById(id);
        HttpStatus status;
        if (null != room){
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Room>(room, status);
    }

    //Get single Room by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Room>> getRoom(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        Room room = roomService.getSingleRoomById(id);
        Resource<Room> resource = new Resource<>(room);
        HttpStatus status;
        if (null != room){
            if (thisUser.getRole().getRoleName().equals("ADMIN")) {
                resource.add(linkTo(methodOn(RoomController.class).deleteRoom(room.getObjectId())).withRel("delete"));
            }
            //resource.add(linkTo(methodOn(RoomController.class).updateRoom(room.getObjectId(), room)).withRel("update"));
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Room>> response = new ResponseEntity<Resource<Room>>(resource, status);
        return response;
    }

    //Create room, fetch into database
    @CacheRemoveAll(cacheName = "roomList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createRoom(@RequestBody RoomDTO roomDTO, HttpServletRequest request){
        User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(sessionUser, roomDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        try {
            iudAnswer = roomService.insertRoom(roomDTO);
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

    //Update room method
    @CacheRemoveAll(cacheName = "roomList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateRoom(@PathVariable("id") Integer id, @RequestBody RoomDTO changedRoom, HttpServletRequest request){
        User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(sessionUser, changedRoom);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        try {
            iudAnswer = roomService.updateRoom(id, changedRoom);
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

    //Delete room method
    @CacheRemoveAll(cacheName = "roomList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteRoom(@PathVariable("id") Integer id){
        IUDAnswer result = null;
        try {
            result = roomService.deleteRoom(id);
        } catch (ReferenceBreakException e) {
            LOGGER.warn(ON_ENTITY_REFERENCE, e);
            return new ResponseEntity<IUDAnswer>( new IUDAnswer(id,false, ON_ENTITY_REFERENCE, e.getMessage()), HttpStatus.BAD_REQUEST);
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
        if (result != null && result.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
