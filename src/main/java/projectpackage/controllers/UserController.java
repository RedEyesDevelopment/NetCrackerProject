package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.model.auth.User;
import projectpackage.service.authservice.UserService;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController(value = "/users")
public class UserController {

    @Autowired
    UserService userService;

    //Get User List with Sortings
    @ResponseStatus(HttpStatus.OK)
    @CacheResult(cacheName = "userList")
    @GetMapping(value = "/{sortingParameter}&{sortingOrder}",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<Resource<User>> getUserList(@PathVariable("sortingParameter") String sortingParameter, @PathVariable("sortingOrder") String sortingOrderString){
        //Parse boolean String "ASCEND"
        Boolean sortingOrder = Boolean.parseBoolean(sortingOrderString);
        //Get users from service
        List<User> users = userService.getAllUsers(sortingParameter,sortingOrder);
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
    //	@Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
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
    @PostMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> createUser(@RequestBody User newUser){
        //Creating RESPONSEENTITY - special class for responsing with object and HttpStatusCode
        Boolean result = userService.insertUser(newUser);
        //Making status object for result boolean
        HttpStatus status;
        if (result) {
            status = HttpStatus.CREATED;
        } else status = HttpStatus.BAD_REQUEST;
        //Creating simple ResponseEntity
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(result, status);
        return responseEntity;
    }

    //Update user method
    //	@Secured("ROLE_ADMIN")
    @CacheRemoveAll(cacheName = "userList")
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> updateUser(@PathVariable("id") Integer id, @RequestBody User changedUser){
        //Validating link pathVariable ID is equal to changedUser ID
        if (!id.equals(changedUser.getObjectId())){
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        //Creating RESPONSEENTITY - special class for responsing with object and HttpStatusCode
        Boolean result = userService.updateUser(id, changedUser);
        //Making status object for result boolean
        HttpStatus status;
        if (result) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.BAD_REQUEST;
        //Creating simple ResponseEntity
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(result, status);
        return responseEntity;
    }

    //Delete user method
    //	@Secured("ROLE_ADMIN")
    @CacheRemoveAll(cacheName = "userList")
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> deleteUser(@PathVariable("id") Integer id){
        //Creating RESPONSEENTITY - special class for responsing with object and HttpStatusCode
        Boolean result = userService.deleteUser(id);
        //Making status object for result boolean
        HttpStatus status;
        if (result) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.NOT_FOUND;
        //Creating simple ResponseEntity
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(result, status);
        return responseEntity;
    }
}
