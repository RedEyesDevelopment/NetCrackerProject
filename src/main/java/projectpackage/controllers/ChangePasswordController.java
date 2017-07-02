package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projectpackage.service.linksservice.PasswordChangeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cpass")
public class ChangePasswordController {

    @Autowired
    PasswordChangeService passwordChangeService;

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
}
