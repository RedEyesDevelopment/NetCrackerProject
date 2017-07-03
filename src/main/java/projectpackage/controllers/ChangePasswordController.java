package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectpackage.dto.LoginDTO;
import projectpackage.model.auth.User;
import projectpackage.service.authservice.UserService;
import projectpackage.service.linksservice.PasswordChangeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cpass")
public class ChangePasswordController {

    @Autowired
    PasswordChangeService passwordChangeService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{dynamic}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<String> changePassword(@PathVariable("dynamic") String dynamic, HttpServletRequest request) throws ServletException {
        boolean result = passwordChangeService.verifyDynamicLink(dynamic);
        String resultString;
        HttpStatus status;
        if (result){
            status = HttpStatus.OK;
            resultString = "Success";
        } else {
            status = HttpStatus.BAD_REQUEST;
            resultString = "Fail";
        }
            return new ResponseEntity<String>(resultString, status);
    }

    @RequestMapping(value = "/for", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<String> changeFor(@RequestBody LoginDTO login) throws ServletException {
        System.out.println("login="+login);
        User user = userService.getSingleUserByUsername(login.getEmail());
        String response;
        System.out.println("IN PC CONTROLLER, USER IS "+user);
        if (null!=user){
            passwordChangeService.createPasswordChangeTarget(user);
            response = "Email was sended.";
        } else {
            response = "There are no user with such email.";
        }
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
