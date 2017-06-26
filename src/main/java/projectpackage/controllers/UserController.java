package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.IUDAnswer;
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
            if (thisUser.getRole().getRoleName().equals("ADMIN")) resource.add(linkTo(methodOn(UserController.class).deleteUser(thisUser.getObjectId())).withRel("delete"));
            resource.add(linkTo(methodOn(UserController.class).updateUser(thisUser.getObjectId(), thisUser)).withRel("update"));
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

    //Delete user method
    @CacheRemoveAll(cacheName = "userList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<IUDAnswer> deleteUser(@PathVariable("id") Integer id){
        IUDAnswer result = userService.deleteUser(id);
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.OK;
        } else status = HttpStatus.NOT_FOUND;
        ResponseEntity<IUDAnswer> responseEntity = new ResponseEntity<IUDAnswer>(result, status);
        return responseEntity;
    }

    //returned user-client from session
    @RequestMapping(value = "/myself")
    public ResponseEntity<Resource<User>> getUser(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("USER");
        Resource<User> resource;
        if (null!=user){
            resource = new Resource<User>(user);
        } else {
            resource = new Resource<>(new User());
        }

        return new ResponseEntity<Resource<User>>(resource, HttpStatus.OK);
    }
}
