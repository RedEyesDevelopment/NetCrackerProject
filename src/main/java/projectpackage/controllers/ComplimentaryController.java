package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;
import projectpackage.service.maintenanceservice.ComplimentaryService;

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
            if (thisUser.getRole().getRoleName().equals("ADMIN")) {
                resource.add(linkTo(methodOn(ComplimentaryController.class).deleteComplimentary(Complimentary.getObjectId())).withRel("delete"));
            }
            resource.add(linkTo(methodOn(ComplimentaryController.class).updateComplimentary(Complimentary.getObjectId(), Complimentary)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<Resource<Complimentary>> response = new ResponseEntity<Resource<Complimentary>>(resource, status);
        return response;
    }

    @CacheRemoveAll(cacheName = "complimentaryList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createComplimentary(@RequestBody Complimentary newComplimentary) {
        IUDAnswer result = complimentaryService.insertComplimentary(newComplimentary);

        HttpStatus status = result.isSuccessful() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "complimentaryList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateComplimentary(@PathVariable("id") Integer id, @RequestBody Complimentary changedComplimentary) {
        if (!id.equals(changedComplimentary.getObjectId())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, MessageBook.WRONG_UPDATE_ID),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        IUDAnswer result = complimentaryService.updateComplimentary(id, changedComplimentary);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "complimentaryList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteComplimentary(@PathVariable("id") Integer id) {
        IUDAnswer result = complimentaryService.deleteComplimentary(id);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;

        return new ResponseEntity<IUDAnswer>(result, status);
    }
}
