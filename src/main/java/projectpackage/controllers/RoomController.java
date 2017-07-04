package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.RoomDTO;
import projectpackage.model.auth.User;
import projectpackage.model.rooms.Room;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.roomservice.RoomService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

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
    public ResponseEntity<IUDAnswer> createRoom(@RequestBody RoomDTO roomDTO){
        IUDAnswer result = roomService.insertRoom(roomDTO);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update room method
    @CacheRemoveAll(cacheName = "roomList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateRoom(@PathVariable("id") Integer id, @RequestBody RoomDTO changedRoom){
        IUDAnswer result = roomService.updateRoom(id, changedRoom);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete room method
    @CacheRemoveAll(cacheName = "roomList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteRoom(@PathVariable("id") Integer id){
        IUDAnswer result = roomService.deleteRoom(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
