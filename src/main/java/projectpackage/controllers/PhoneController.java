package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.dto.IUDAnswer;
import projectpackage.service.MessageBook;
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
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
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
    public ResponseEntity<Resource<Phone>> getPhone(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        Phone phone = phoneService.getSinglePhoneById(id);
        Resource<Phone> resource = new Resource<>(phone);
        HttpStatus status;
        if (null != phone) {
            if (thisUser.getRole().equals("ADMIN")) {
                resource.add(linkTo(methodOn(PhoneController.class).deletePhone(phone.getObjectId())).withRel("delete"));
            }
            resource.add(linkTo(methodOn(PhoneController.class).updatePhone(phone.getObjectId(), phone)).withRel("update"));
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Phone>> response = new ResponseEntity<Resource<Phone>>(resource, status);
        return response;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createPhone(@RequestBody Phone newPhone) {
        IUDAnswer result = phoneService.insertPhone(newPhone);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updatePhone(@PathVariable("id") Integer id, @RequestBody Phone changedPhone) {
        if (!id.equals(changedPhone.getObjectId())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, MessageBook.WRONG_UPDATE_ID), HttpStatus.NOT_ACCEPTABLE);
        }

        IUDAnswer result = phoneService.updatePhone(id, changedPhone);

        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deletePhone(@PathVariable("id") Integer id) {
        IUDAnswer result = phoneService.deletePhone(id);

        HttpStatus status = result.isSuccessful() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;

        return new ResponseEntity<IUDAnswer>(result, status);
    }
}
