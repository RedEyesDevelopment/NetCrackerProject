package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.model.support.IUDAnswer;
import projectpackage.service.authservice.UserService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
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
        //Get users from service
        List<User> users = userService.getAllUsers();
        //Create Resources for usersList
        List<Resource<User>> resources = new ArrayList<>();
        for (User user:users){
            Resource<User> userResource = new Resource<User>(user);
            //Add GET link for each user
            userResource.add(linkTo(methodOn(UserController.class).getUser(user.getObjectId())).withSelfRel());
            resources.add(userResource);
        }
        return resources;
    }

    //Get single User by id
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Resource<User> getUser(@PathVariable("id") Integer id){
        //PATHVARIABLE is not optional, so every times it returns a value
        User user = userService.getSingleUserById(id);
        //Create resource for user and add links to delete and update
        Resource<User> resource = new Resource<>(user);
        resource.add(linkTo(methodOn(UserController.class).deleteUser(user.getObjectId())).withRel("delete"));
        resource.add(linkTo(methodOn(UserController.class).updateUser(user.getObjectId(), user)).withRel("update"));
        return resource;
    }

    //Create user, fetch into database
    @CacheRemoveAll(cacheName = "userList")
    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> createUser(@RequestBody User newUser){
        //Creating RESPONSEENTITY - special class for responsing with object and HttpStatusCode
        IUDAnswer result = userService.insertUser(newUser);
        //Making status object for result boolean
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        //Creating simple ResponseEntity
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(result.isSuccessful(), status);
        return responseEntity;
    }

    //Update user method
    //	@Secured("ROLE_ADMIN")
    @CacheRemoveAll(cacheName = "userList")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> updateUser(@PathVariable("id") Integer id, @RequestBody User changedUser){
        //Validating link pathVariable ID is equal to changedUser ID
        if (!id.equals(changedUser.getObjectId())){
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        //Creating RESPONSEENTITY - special class for responsing with object and HttpStatusCode
        IUDAnswer result = userService.updateUser(id, changedUser);
        //Making status object for result boolean
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        //Creating simple ResponseEntity
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(result.isSuccessful(), status);
        return responseEntity;
    }

    //Delete user method
    //	@Secured("ROLE_ADMIN")
    @CacheRemoveAll(cacheName = "userList")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> deleteUser(@PathVariable("id") Integer id){
        //Creating RESPONSEENTITY - special class for responsing with object and HttpStatusCode
        IUDAnswer result = userService.deleteUser(id);
        //Making status object for result boolean
        HttpStatus status;
        if (result.isSuccessful()) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        //Creating simple ResponseEntity
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(result.isSuccessful(), status);
        return responseEntity;
    }
}
