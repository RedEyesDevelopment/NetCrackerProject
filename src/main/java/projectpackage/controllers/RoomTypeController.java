package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.model.rooms.RoomType;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.roomservice.RoomTypeService;

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
@RequestMapping("/roomtypes")
public class RoomTypeController {
    @Autowired
    RoomTypeService roomTypeService;

    //Get RoomType List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "roomTypeList")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
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

    //Get single RoomType by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<RoomType>> getRoomType(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
        RoomType roomType = roomTypeService.getSingleRoomTypeById(id);
        Resource<RoomType> resource = new Resource<>(roomType);
        HttpStatus status;
        if (null != roomType){
            if (thisUser.getRole().getRoleName().equals("ADMIN")) resource.add(linkTo(methodOn(RoomTypeController.class).deleteRoomType(roomType.getObjectId())).withRel("delete"));
            resource.add(linkTo(methodOn(RoomTypeController.class).updateRoomType(roomType.getObjectId(), roomType)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<RoomType>> response = new ResponseEntity<Resource<RoomType>>(resource, status);
        return response;
    }

    //Create roomType, fetch into database
    @CacheRemoveAll(cacheName = "roomTypeList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createRoomType(@RequestBody RoomType newRoomType){
        IUDAnswer result = roomTypeService.insertRoomType(newRoomType);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update roomType method
    @CacheRemoveAll(cacheName = "roomTypeList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateRoomType(@PathVariable("id") Integer id, @RequestBody RoomType changedRoomType){
        if (!id.equals(changedRoomType.getObjectId())){
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, "wrongId"), HttpStatus.NOT_ACCEPTABLE);
        }
        IUDAnswer result = roomTypeService.updateRoomType(id, changedRoomType);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        //Creating simple ResponseEntity
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete roomType method
    @CacheRemoveAll(cacheName = "roomTypeList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteRoomType(@PathVariable("id") Integer id){
        IUDAnswer result = roomTypeService.deleteRoomType(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
