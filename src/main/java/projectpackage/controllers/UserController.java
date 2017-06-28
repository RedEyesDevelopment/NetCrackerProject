package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
import projectpackage.dto.UserBasicDTO;
import projectpackage.dto.UserPasswordDTO;
import projectpackage.model.auth.Role;
import projectpackage.model.auth.User;
import projectpackage.service.MessageBook;
import projectpackage.service.authservice.UserService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static projectpackage.service.MessageBook.NULL_ID;
import static projectpackage.service.MessageBook.SAME_PASSWORDS;
import static projectpackage.service.MessageBook.WRONG_FIELD;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    //Get User List
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "userList")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<User>> getUserList(){
        List<User> users = userService.getAllUsers();
        List<Resource<User>> resources = new ArrayList<>();
        for (User user:users){
            Resource<User> userResource = new Resource<User>(user);
            userResource.add(linkTo(methodOn(UserController.class).getUser(user.getObjectId(), null)).withSelfRel());
            resources.add(userResource);
        }
        return resources;
    }

    //Get single User by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<User>> getUser(@PathVariable("id") Integer id, HttpServletRequest request){
        User thisUser = (User) request.getSession().getAttribute("USER");
//        User user = userService.getSingleUserById(id);
        Resource<User> resource = new Resource<>(thisUser);
        HttpStatus status;
        if (null != thisUser){
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<Resource<User>> response = new ResponseEntity<Resource<User>>(resource, status);
        return response;
    }

    //Create user, fetch into database
    @CacheRemoveAll(cacheName = "userList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> createUser(@RequestBody User newUser) {
        IUDAnswer result = userService.insertUser(newUser);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = {MediaType
            .APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> registrationUser(@RequestBody User newUser) {
        Role role = new Role();
        role.setObjectId(3);
        role.setRoleName("CLIENT");
        newUser.setRole(role);
        newUser.setEnabled(Boolean.TRUE);
        IUDAnswer result = userService.insertUser(newUser);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //Update user method
    @CacheRemoveAll(cacheName = "userList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateUser(@PathVariable("id") Integer id, @RequestBody User changedUser){
        if (!id.equals(changedUser.getObjectId())){
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(id, false, MessageBook.WRONG_UPDATE_ID), HttpStatus.NOT_ACCEPTABLE);
        }
        IUDAnswer result = userService.updateUser(id, changedUser);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.BAD_REQUEST;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    @RequestMapping(value = "/update/password/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updatePassword(@PathVariable("id") Integer id, @RequestBody UserPasswordDTO userPasswordDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        if (id == null || id.equals(sessionUser.getObjectId())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, MessageBook.NULL_ID), HttpStatus.BAD_REQUEST);
        } else if (userPasswordDTO == null || userPasswordDTO.getNewPassword() == null || userPasswordDTO.getOldPassword() == null) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, WRONG_FIELD), HttpStatus.BAD_REQUEST);
        } else if (userPasswordDTO.getOldPassword().equals(userPasswordDTO.getNewPassword())) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, SAME_PASSWORDS), HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setAdditionalInfo(sessionUser.getAdditionalInfo());
        user.setEnabled(Boolean.TRUE);
        user.setFirstName(sessionUser.getFirstName());
        user.setLastName(sessionUser.getLastName());
        user.setEmail(sessionUser.getEmail());
        user.setPassword(userPasswordDTO.getNewPassword());
        user.setPhones(sessionUser.getPhones());
        user.setRole(sessionUser.getRole());
        IUDAnswer answer = userService.updateUser(id, user);
        if (answer.isSuccessful()) {
            request.getSession().removeAttribute("USER");
            request.getSession().setAttribute("USER", userService.getSingleUserById(id));
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
        }

        return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/update/basic/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> updateBasicInfo(@PathVariable("id") Integer id, @RequestBody UserBasicDTO userBasicDTO, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("USER");
        if (id == null || id.intValue() != sessionUser.getObjectId()) {
            return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, MessageBook.NULL_ID), HttpStatus.BAD_REQUEST);
        }
        User user = userService.getSingleUserById(id);
        IUDAnswer answer = userService.updateUser(id, user);
        if (answer.isSuccessful()) {
            request.getSession().removeAttribute("USER");
            request.getSession().setAttribute("USER", userService.getSingleUserById(id));
            return new ResponseEntity<IUDAnswer>(answer, HttpStatus.OK);
        }

        return new ResponseEntity<IUDAnswer>(answer, HttpStatus.BAD_REQUEST);
    }

    //Delete user method
    @CacheRemoveAll(cacheName = "userList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteUser(@PathVariable("id") Integer id){
        if (id == null) return new ResponseEntity<IUDAnswer>(new IUDAnswer(false, NULL_ID), HttpStatus.BAD_REQUEST);
        IUDAnswer result = userService.deleteUser(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //returned user-client from session
    @RequestMapping(value = "/myself")
    public ResponseEntity<User> getUser(HttpServletRequest request){
        Integer userId = ((User) request.getSession().getAttribute("USER")).getObjectId();
        HttpStatus status;
        User user = null;
        if (null!=userId){
            user = userService.getSingleUserById(userId);
            user.setPassword(null);
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<User>(user, status);
    }
}
