package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.authservice.PhoneService;

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
@RequestMapping("/phones")
public class PhoneController {

    @Autowired
    PhoneService phoneService;

    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "phoneList")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<Phone>> getPhoneList() {
        List<Phone> phones = phoneService.getAllPhones();
        List<Resource<Phone>> resources = new ArrayList<>(phones.size());
        for (Phone phone : phones) {
            Resource<Phone> phoneResource = new Resource<>(phone);
            phoneResource.add(linkTo(methodOn(PhoneController.class).getPhone(phone.getObjectId(), null)).withSelfRel());
            resources.add(phoneResource);
        }
        return resources;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Resource<Phone> getPhone(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        Phone phone = phoneService.getSinglePhoneById(id);
        Resource<Phone> resource = new Resource<>(phone);
        if (thisUser.getRole().equals("ADMIN")) {
            resource.add(linkTo(methodOn(PhoneController.class).deletePhone(phone.getObjectId())).withRel("delete"));
        }
        resource.add(linkTo(methodOn(PhoneController.class).updatePhone(phone.getObjectId(), phone)).withRel("update"));
        return resource;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> createPhone(@RequestBody Phone newPhone) {
        IUDAnswer result = phoneService.insertPhone(newPhone);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(result.isSuccessful(), status);
        return responseEntity;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> updatePhone(@PathVariable("id") Integer id, @RequestBody Phone changedPhone) {
        if (!id.equals(changedPhone.getObjectId())) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        IUDAnswer result = phoneService.updatePhone(id, changedPhone);

        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(result.isSuccessful(), status);
        return responseEntity;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> deletePhone(@PathVariable("id") Integer id) {
        IUDAnswer result = phoneService.deletePhone(id);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;

        return new ResponseEntity<Boolean>(result.isSuccessful(), status);
    }
}
