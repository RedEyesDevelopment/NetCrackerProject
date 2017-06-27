package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.Maintenance;
import projectpackage.service.MessageBook;
import projectpackage.service.maintenanceservice.MaintenanceService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Arizel on 28.05.2017.
 */
@RestController
@RequestMapping("/maintenances")
public class MaintenanceController {

    @Autowired
    MaintenanceService maintenanceService;

    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "maintenanceList")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Maintenance> getMaintenanceList() {
        return maintenanceService.getAllMaintenances();

    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Maintenance>> getMaintenance(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");

        Maintenance Maintenance = maintenanceService.getSingleMaintenanceById(id);
        Resource<Maintenance> resource = new Resource<>(Maintenance);
        HttpStatus status;
        if (null != Maintenance) {
            if (thisUser.getRole().getRoleName().equals("ADMIN")) {
                resource.add(linkTo(methodOn(MaintenanceController.class).deleteMaintenance(Maintenance.getObjectId())).withRel("delete"));
            }
            resource.add(linkTo(methodOn(MaintenanceController.class).updateMaintenance(Maintenance.getObjectId(), Maintenance)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<Resource<Maintenance>> response = new ResponseEntity<Resource<Maintenance>>(resource, status);
        return response;
    }

    @CacheRemoveAll(cacheName = "maintenanceList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createMaintenance(@RequestBody Maintenance newMaintenance) {
        IUDAnswer result = maintenanceService.insertMaintenance(newMaintenance);

        HttpStatus status = result.isSuccessful() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "maintenanceList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateMaintenance(@PathVariable("id") Integer id, @RequestBody Maintenance changedMaintenance) {
        if (!id.equals(changedMaintenance.getObjectId())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, MessageBook.WRONG_UPDATE_ID),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        IUDAnswer result = maintenanceService.updateMaintenance(id, changedMaintenance);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<IUDAnswer>(result, status);
    }

    @CacheRemoveAll(cacheName = "maintenanceList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteMaintenance(@PathVariable("id") Integer id) {
        IUDAnswer result = maintenanceService.deleteMaintenance(id);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;

        return new ResponseEntity<IUDAnswer>(result, status);
    }
}
