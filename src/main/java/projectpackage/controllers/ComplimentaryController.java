package projectpackage.controllers;

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
import projectpackage.service.maintenanceservice.ComplimentaryService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Arizel on 28.05.2017.
 */
@RestController
@RequestMapping("/complimentaries")
public class ComplimentaryController {

    @Autowired
    ComplimentaryService complimentaryService;

    @Autowired
    ServiceUtils serviceUtils;

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

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Complimentary>> getComplimentary(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");

        Complimentary Complimentary = complimentaryService.getSingleComplimentaryById(id);
        Resource<Complimentary> resource = new Resource<>(Complimentary);
        HttpStatus status;
        if (null != Complimentary) {
            status = HttpStatus.ACCEPTED;
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
        Complimentary complimentary = new Complimentary();
        Maintenance maintenance = new Maintenance();
        maintenance.setObjectId(complimentaryDTO.getMaintenanceId());
        complimentary.setCategoryId(complimentaryDTO.getCategoryId());
        complimentary.setMaintenance(maintenance);
        IUDAnswer result = complimentaryService.insertComplimentary(complimentary);

        HttpStatus status = result.isSuccessful() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
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
        IUDAnswer result = complimentaryService.updateComplimentary(id, complimentary);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "complimentaryList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteComplimentary(@PathVariable("id") Integer id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDeleteForAdmin(user, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        IUDAnswer result = complimentaryService.deleteComplimentary(id);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;

        return new ResponseEntity<IUDAnswer>(result, status);
    }
}
