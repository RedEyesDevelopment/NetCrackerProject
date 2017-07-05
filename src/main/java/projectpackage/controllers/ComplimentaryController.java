package projectpackage.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.ComplimentaryDTO;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.model.orders.Category;
import projectpackage.repository.support.daoexceptions.DeletedObjectNotExistsException;
import projectpackage.repository.support.daoexceptions.ReferenceBreakException;
import projectpackage.repository.support.daoexceptions.WrongEntityIdException;
import projectpackage.service.maintenanceservice.ComplimentaryService;
import projectpackage.service.orderservice.CategoryService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;
import static projectpackage.service.MessageBook.NULL_ID;

@RestController
@RequestMapping("/complimentaries")
public class ComplimentaryController {

    private static final Logger LOGGER = Logger.getLogger(ComplimentaryController.class);

    @Autowired
    ComplimentaryService complimentaryService;

    @Autowired
    ServiceUtils serviceUtils;

    @Autowired
    CategoryService categoryService;

    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "complimentaryList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Complimentary>> getComplimentaryList() {
        List<Complimentary> Complimentarys = complimentaryService.getAllComplimentaries();
        List<Resource<Complimentary>> resources = new ArrayList<>(Complimentarys.size());

        for (Complimentary Complimentary : Complimentarys) {
            Resource<Complimentary> resource = new Resource<>(Complimentary);
            resource.add(linkTo(methodOn(ComplimentaryController.class).getComplimentary(Complimentary.getObjectId(), null)).withSelfRel());
            resources.add(resource);
        }

        return resources;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Complimentary>> getComplimentary(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");

        Complimentary Complimentary = complimentaryService.getSingleComplimentaryById(id);
        Resource<Complimentary> resource = new Resource<>(Complimentary);
        HttpStatus status;
        if (null != Complimentary) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<Resource<Complimentary>> response = new ResponseEntity<Resource<Complimentary>>(resource, status);
        return response;
    }

    @CacheRemoveAll(cacheName = "complimentaryList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createComplimentary(@RequestBody ComplimentaryDTO complimentaryDTO, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, complimentaryDTO);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Category category = categoryService.getSingleCategoryById(complimentaryDTO.getCategoryId());
        if (category.getComplimentaries() != null && !category.getComplimentaries().isEmpty()){
            for (Complimentary complimentary : category.getComplimentaries()) {
                if (complimentary.getMaintenance().getObjectId() == complimentaryDTO.getMaintenanceId()) {
                    return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, CANNOT_HAVE_DUPLICATE_COMPLIMENTARY), HttpStatus.BAD_REQUEST);
                }
            }
        }
        Complimentary complimentary = new Complimentary();
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(complimentaryDTO.getMaintenanceId());
        complimentary.setCategoryId(complimentaryDTO.getCategoryId());
        complimentary.setMaintenance(maintenance);

        try {
            iudAnswer = complimentaryService.insertComplimentary(complimentary);
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

    @CacheRemoveAll(cacheName = "complimentaryList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateComplimentary(@PathVariable("id") Integer id, @RequestBody ComplimentaryDTO complimentaryDTO, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAdminAndData(user, complimentaryDTO, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        Complimentary complimentary = new Complimentary();
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(complimentaryDTO.getMaintenanceId());
        complimentary.setCategoryId(complimentaryDTO.getCategoryId());
        complimentary.setMaintenance(maintenance);
        try {
            iudAnswer = complimentaryService.updateComplimentary(id, complimentary);
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

    @CacheRemoveAll(cacheName = "complimentaryList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteComplimentary(@PathVariable("id") Integer id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }

        try {
            iudAnswer = complimentaryService.deleteComplimentary(id);
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
