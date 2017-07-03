package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.model.auth.Phone;
import projectpackage.model.auth.User;
import projectpackage.service.authservice.PhoneService;
import projectpackage.service.authservice.UserService;
import projectpackage.service.support.ServiceUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.*;

/**
 * Created by Arizel on 28.05.2017.
 */
@RestController
@RequestMapping("/phones")
public class PhoneController {

    @Autowired
    PhoneService phoneService;

    @Autowired
    UserService userService;

    @Autowired
    ServiceUtils serviceUtils;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Phone>> getPhone(@PathVariable("id") Integer id, HttpServletRequest request) {
        User thisUser = (User) request.getSession().getAttribute("USER");
        Phone phone = phoneService.getSinglePhoneById(id);
        Resource<Phone> resource = new Resource<>(phone);
        HttpStatus status;
        if (null != phone) {
            //resource.add(linkTo(methodOn(PhoneController.class).updatePhone(phone.getObjectId(), phone)).withRel("update"));
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<Phone>> response = new ResponseEntity<Resource<Phone>>(resource, status);
        return response;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createPhone(@RequestBody Phone newPhone, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAndData(user, newPhone);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        if (serviceUtils.isClient(user)) {
            if (newPhone.getUserId() != user.getObjectId()) {
                return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, INVALID_USER_ID), HttpStatus.BAD_REQUEST);
            }
        }

        IUDAnswer result = phoneService.insertPhone(newPhone);
        HttpStatus status;
        if ((result.isSuccessful() && !serviceUtils.isAdmin(user)) || user.getObjectId() == newPhone.getUserId()) {
            request.getSession().removeAttribute("USER");
            request.getSession().setAttribute("USER", userService.getSingleUserById(user.getObjectId()));
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updatePhone(@PathVariable("id") Integer id, @RequestBody Phone changedPhone, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkSessionAndData(user, changedPhone);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        if (!serviceUtils.isAdmin(user)) {
            if (changedPhone.getUserId() != user.getObjectId()) {
                return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, INVALID_USER_ID), HttpStatus.BAD_REQUEST);
            }
        }

        IUDAnswer result = phoneService.updatePhone(id, changedPhone);
        HttpStatus status;
        if ((result.isSuccessful() && !serviceUtils.isAdmin(user)) || user.getObjectId() == changedPhone.getUserId()) {
            request.getSession().removeAttribute("USER");
            request.getSession().setAttribute("USER", userService.getSingleUserById(id));
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @CacheRemoveAll(cacheName = "phoneList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deletePhone(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        IUDAnswer iudAnswer = serviceUtils.checkDelete(sessionUser, id);
        if (!iudAnswer.isSuccessful()) {
            return new ResponseEntity<IUDAnswer>(iudAnswer, HttpStatus.BAD_REQUEST);
        }
        if (!serviceUtils.isAdmin(sessionUser)) {
            if (sessionUser.getPhones().size() <= 1) {
                return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, TRY_TO_DELETE_LAST_PHONE), HttpStatus.BAD_REQUEST);
            }
            boolean isBelongToUser = false;
            for (Phone phone : sessionUser.getPhones()) {
                if (phone.getUserId() == sessionUser.getObjectId()) {
                    isBelongToUser = true;
                    break;
                }
            }
            if (!isBelongToUser) {
                return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, PHONE_NOT_BELONG_TO_USER), HttpStatus.BAD_REQUEST);
            }
        } else {
            User user = userService.getSingleUserById(phoneService.getSinglePhoneById(id).getUserId());
            if (user.getPhones().size() <= 1) {
                return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, TRY_TO_DELETE_LAST_PHONE), HttpStatus.BAD_REQUEST);
            }
        }

        IUDAnswer result = phoneService.deletePhone(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            request.getSession().removeAttribute("USER");
            request.getSession().setAttribute("USER", userService.getSingleUserById(sessionUser.getObjectId()));
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<IUDAnswer>(result, status);
    }
}
