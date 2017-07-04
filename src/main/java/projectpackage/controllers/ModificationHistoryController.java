package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.model.orders.ModificationHistory;
import projectpackage.model.orders.Order;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.orderservice.ModificationHistoryService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/histories")
public class ModificationHistoryController {

    @Autowired
    ModificationHistoryService modificationHistoryService;

    //Get ModificationHistory List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "modificationHistoryList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<ModificationHistory>> getModificationHistoryList() {
        List<ModificationHistory> modificationHistorys = modificationHistoryService.getAllModificationHistory();
        List<Resource<ModificationHistory>> resources = new ArrayList<>();
        for (ModificationHistory modificationHistory : modificationHistorys) {
            Resource<ModificationHistory> modificationHistoryResource = new Resource<ModificationHistory>(modificationHistory);
            modificationHistoryResource.add(linkTo(methodOn(ModificationHistoryController.class).getModificationHistory(modificationHistory.getObjectId(), null)).withSelfRel());
            resources.add(modificationHistoryResource);
        }
        return resources;
    }

    //Get single ModificationHistory by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<ModificationHistory>> getModificationHistory(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        ModificationHistory modificationHistory = modificationHistoryService.getSingleModificationHistoryById(id);
        Resource<ModificationHistory> resource = new Resource<>(modificationHistory);
        HttpStatus status;
        if (null != modificationHistory) {
            if (thisUser.getRole().getRoleName().equals("ADMIN"))
                resource.add(linkTo(methodOn(ModificationHistoryController.class).deleteModificationHistory(modificationHistory.getObjectId())).withRel("delete"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<ModificationHistory>> response = new ResponseEntity<Resource<ModificationHistory>>(resource, status);
        return response;
    }

    //Create modificationHistory, fetch into database
    @CacheRemoveAll(cacheName = "modificationHistoryList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createModificationHistory(@RequestBody Order newOrder, @RequestBody Order oldOrder) {
        IUDAnswer result = modificationHistoryService.insertModificationHistory(newOrder, oldOrder);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Delete modificationHistory method
    @CacheRemoveAll(cacheName = "modificationHistoryList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteModificationHistory(@PathVariable("id") Integer id) {
        IUDAnswer result = modificationHistoryService.deleteModificationHistory(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }
}
