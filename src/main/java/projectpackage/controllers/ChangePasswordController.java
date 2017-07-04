package projectpackage.controllers;

import org.apache.log4j.Logger;
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

import static projectpackage.service.MessageBook.WRONG_FIELD;

@RestController
@RequestMapping("/cpass")
public class ChangePasswordController {

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordController.class);

    @Autowired
    PasswordChangeService passwordChangeService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{dynamic}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<String> changePassword(@PathVariable("dynamic") String dynamic, HttpServletRequest request) throws ServletException   {
        boolean result = false;
        try {
            result = passwordChangeService.verifyDynamicLink(dynamic);
        } catch (IllegalArgumentException e) {
            LOGGER.warn(WRONG_FIELD, e);
            return new ResponseEntity<String>(WRONG_FIELD, HttpStatus.BAD_REQUEST);
        }
        String resultString;
        HttpStatus status;
        if (result){
            status = HttpStatus.OK;
            resultString = "Success :)";
        } else {
            status = HttpStatus.BAD_REQUEST;
            resultString = "Fail :(";
        }
        return new ResponseEntity<String>(resultString, status);
    }

    @RequestMapping(value = "/for", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Boolean> changeFor(@RequestBody LoginDTO login) throws ServletException {
        User user = userService.getSingleUserByUsername(login.getEmail());
        if (null!=user){
            passwordChangeService.createPasswordChangeTarget(user);
            return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
        } else {
            return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.BAD_REQUEST);
        }
    }
}
